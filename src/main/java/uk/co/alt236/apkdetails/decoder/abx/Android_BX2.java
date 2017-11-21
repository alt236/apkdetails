/*
 * Copyright (C) 2012 Prasanta Paul, http://prasanta-paul.blogspot.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.alt236.apkdetails.decoder.abx;

import uk.co.alt236.apkdetails.decoder.utils.Log;
import uk.co.alt236.apkdetails.decoder.utils.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.co.alt236.apkdetails.decoder.abx.Resource.*;


/**
 * Android Binary XML format
 * # Help
 * # http://justanapplication.wordpress.com/category/android/android-binary-xml/
 * # frameworks/base/include/utils/ResourceTypes.h
 * <p>
 * TODO:
 * 1. CDATA, comment handling.
 * 2. Resource Table (.arsc file)
 * 3. Mapping Attribute data with Resource Table entries
 *
 * @author Prasanta Paul
 */
public class Android_BX2 {

    private byte[] chunk_type_buf = new byte[2];
    private byte[] header_size_buf = new byte[2];
    private byte[] chunk_size_buf = new byte[4];
    private int header_size;
    private int chunk_size;
    private int package_count;

    private byte[] buf_2 = new byte[2];
    private byte[] buf_4 = new byte[4];

    private String tag = "Android_BX2";

    /**
     * Binary XML String pool
     */
    private List<String> stringPool = new ArrayList<>();
    /**
     * Resources.arsc Resource table string pool
     */
    private List<String> resStringPool = new ArrayList<>();
    private Map<String, String> namespaceLookup = new HashMap<>();

    /**
     * Resource Map
     */
    private ArrayList<Integer> resMap = new ArrayList<Integer>();

    private int ns_prefix_index = -1;
    private int ns_uri_index = -1;
    private int ns_linenumber = 0;

    /**
     * Order of XML node. Used by XML generator to track root node should include XML Namespace definition
     * and children should not.
     */
    private int nodeIndex = -1;

    private BXCallback listener = null;

    public Android_BX2(BXCallback listner) {
        this.listener = listner;
    }


    public void parse(byte[] binaryXml) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(binaryXml);
        BufferedInputStream in = new BufferedInputStream(bais);

        if (listener != null)
            listener.startDoc(null);

        parse(in);
    }

    /**
     * Parse XML resourcs...
     * <p>
     * [String Pool]
     * [Resource Map]
     * [Namespace Start]
     * [XML Start]
     * [XML End]
     * [XML Start]
     * [XML End]
     * .....
     * [Namespace End]
     * * [Namespace Start]
     * [XML Start]
     * [XML End]
     * [XML Start]
     * [XML End]
     * .....
     * [Namespace End]
     * ....
     * # There can be multiple Namespace and within one Name space multiple XML nodes.
     *
     * @param bxFile
     * @throws Exception
     */
    public void parse(String bxFile) throws Exception {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(bxFile));

        if (listener != null)
            listener.startDoc(bxFile);

        parse(in);
    }

    private void parse(BufferedInputStream in) throws Exception {
        int header_size;
        int chunk_size;

        // Is it an valid BXML ?
        /*
         * Chunk header meta size - 8 bytes
		 * [Chunk Type] - 2 bytes
		 * [Chunk Header Size] - 2 bytes
		 * [Chunk Size] - 4 bytes
		 */
        read(in, chunk_type_buf);

        if (Utils.toInt(chunk_type_buf, false) != RES_XML_TYPE) {
            Log.p(tag, "It's an invalid BXML file. Exiting!");
            return;
        }


        read(in, header_size_buf);
        header_size = Utils.toInt(header_size_buf, false);

        read(in, chunk_size_buf);
        chunk_size = Utils.toInt(chunk_size_buf, false);

        Log.d(tag, "Header Size: " + header_size + " Chunk size: " + chunk_size);

        read(in, chunk_type_buf);

        if (Utils.toInt(chunk_type_buf, false) == RES_STRING_POOL_TYPE) {
            // String Pool/Tokens
            Log.d(tag, "String Pool...");
            read(in, header_size_buf);
            header_size = Utils.toInt(header_size_buf, false);

            read(in, chunk_size_buf);
            chunk_size = Utils.toInt(chunk_size_buf, false);

            Log.d(tag, "String Pool...Header Size: " + header_size + " Chunk Size: " + chunk_size);

            byte[] spBuf = new byte[chunk_size - 8];
            read(in, spBuf);

            // Parse String pool
            parseStringPool(spBuf, header_size, chunk_size);

            // Get the next Chunk
            read(in, chunk_type_buf);
        }

        // Resource Mapping- Optional Content
        if (Utils.toInt(chunk_type_buf, false) == RES_XML_RESOURCE_MAP_TYPE) {
            read(in, header_size_buf);
            header_size = Utils.toInt(header_size_buf, false);

            read(in, chunk_size_buf);
            chunk_size = Utils.toInt(chunk_size_buf, false);

            byte[] rmBuf = new byte[chunk_size - 8];
            read(in, rmBuf);

            // Parse Resource Mapping
            parseResMapping(rmBuf, header_size, chunk_size);

            // Get the next Chunk
            read(in, chunk_type_buf);
        }

		/*
		 * There can be multiple Name space and XML node sections
		 * [XML_NameSpace_Start]
		 * 	[XML_Start]
		 *  	[XML_Start]
		 * 		[XML_End]
		 *  [XML_END]
		 * [XML_NameSpace_End]
		 * [XML_NameSpace_Start]
		 * 	[XML_Start]
		 * 	[XML_End]
		 * [XML_NameSpace_End]
		 */

        // Name space Start
        if (Utils.toInt(chunk_type_buf, false) == RES_XML_START_NAMESPACE_TYPE) {
            read(in, header_size_buf);
            header_size = Utils.toInt(header_size_buf, false);

            read(in, chunk_size_buf);
            chunk_size = Utils.toInt(chunk_size_buf, false);

            byte[] nsStartBuf = new byte[chunk_size - 8];
            read(in, nsStartBuf);

            // Parse Start of Name space
            parseStartNameSpace(nsStartBuf, header_size, chunk_size);
        }

        // Handle multiple XML Elements
        read(in, chunk_type_buf);
        int chunk_type = Utils.toInt(chunk_type_buf, false);

        while (chunk_type != RES_XML_END_NAMESPACE_TYPE) {
            Log.d(tag, "Parsing XML node...Chunk_Type " + chunk_type);
			/*
			 * XML_Start
			 * 	XML_Start
			 *  XML_End
			 * XML_End
			 * .......
			 */
            read(in, header_size_buf);
            header_size = Utils.toInt(header_size_buf, false);

            read(in, chunk_size_buf);
            chunk_size = Utils.toInt(chunk_size_buf, false);

            byte[] elementBuf = new byte[chunk_size - 8];
            read(in, elementBuf);

            if (chunk_type == RES_XML_START_ELEMENT_TYPE) {
                // Start of XML Node
                parseXMLStart(elementBuf, header_size, chunk_size);
            } else if (chunk_type == RES_XML_END_ELEMENT_TYPE) {
                // End of XML Node
                parseXMLEnd(elementBuf, header_size, chunk_size);
            }

            // TODO: CDATA

            // Next Chunk type
            read(in, chunk_type_buf);
            chunk_type = Utils.toInt(chunk_type_buf, false);
        }

        // End of Name space
        if (chunk_type == RES_XML_END_NAMESPACE_TYPE) {
            read(in, header_size_buf);
            header_size = Utils.toInt(header_size_buf, false);

            read(in, chunk_size_buf);
            chunk_size = Utils.toInt(chunk_size_buf, false);

            byte[] nsEndBuf = new byte[chunk_size - 8];
            read(in, nsEndBuf);

            // Parse End of Name space
            parseEndNameSpace(nsEndBuf, header_size, chunk_size);
        }

        if (listener != null)
            listener.endDoc();

        // That's it. TODO: Handle multiple Name spaces
    }

    /**
     * String pool Header
     * Chunk Header - 8 bytes
     * String count - init32
     * Style count - init32
     * Flag (1/8) - init32
     * 1 - Is it sorted
     * 8 - Is it UTF-8 encoded
     * String start - init32
     * Style start - init32
     * <p>
     * String pool Data
     * [SP indices (init32) for each String.] [Style indices (init32) for each style]
     * [String Len][String][0x0000]
     * ......
     * [Style span data] - TODO.
     *
     * @param spBuf - byte array of StringPool
     */
    private void parseStringPool(byte[] spBuf, int header_size, int chunk_size) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(spBuf);

        // String pool header
        byte[] int_buf = new byte[4];
        read(in, int_buf);

        int string_count = Utils.toInt(int_buf, false);
        read(in, int_buf);
        int style_count = Utils.toInt(int_buf, false);
        read(in, int_buf);
        int flag = Utils.toInt(int_buf, false);
        read(in, int_buf);
        int string_start = Utils.toInt(int_buf, false);
        read(in, int_buf);
        int style_start = Utils.toInt(int_buf, false);

        Log.d(tag, "String Count: " + string_count + " Style Count: " + style_count + " Flag: " + flag + " String Start: " + string_start + " Style Start: " + style_start);

        // String pool data
        // Read index location of each String
        int[] string_indices = new int[string_count];
        if (string_count > 0) {
            for (int i = 0; i < string_count; i++) {
                read(in, int_buf);
                string_indices[i] = Utils.toInt(int_buf, false);
            }
        }

        if (style_count > 0) {
            // Skip Style
            in.skip(style_count * 4);
        }

        // Read Strings
        for (int i = 0; i < string_count; i++) {
            int string_len = 0;
            if (i == string_count - 1) {
                if (style_start == 0)// There is no Style span
                {
                    // Length of the last string. Chunk Size - Start position of this String - Header - Len of Indices
                    string_len = chunk_size - string_indices[i] - header_size - 4 * string_count;
                    Log.d(tag, "Last String size: " + string_len + " Chunk_Size: " + chunk_size + " Index: " + string_indices[i]);
                } else
                    string_len = style_start - string_indices[i];
            } else
                string_len = string_indices[i + 1] - string_indices[i];

			/*
			 * Each String entry contains Length header (2 bytes to 4 bytes) + Actual String + [0x00]
			 * Length header sometime contain duplicate values e.g. 20 20
			 * Actual string sometime contains 00, which need to be ignored
			 * Ending zero might be  2 byte or 4 byte
			 *
			 * TODO: Consider both Length bytes and String length > 32767 characters
			 */
            byte[] short_buf = new byte[2];
            read(in, short_buf);
            int actual_str_len = 0;
            if (short_buf[0] == short_buf[1]) // Its repeating, happens for Non-Manifest file. e.g. 20 20
                actual_str_len = short_buf[0];
            else
                actual_str_len = Utils.toInt(short_buf, false);

            byte[] str_buf = new byte[actual_str_len];
            byte[] buf = new byte[string_len - 2]; // Skip 2 Length bytes, already read.
            read(in, buf);
            int j = 0;
            for (int k = 0; k < buf.length; k++) {
                // Skipp 0x00
                if (buf[k] != 0x00)
                    str_buf[j++] = buf[k];
            }

            stringPool.add(new String(str_buf));
        }

        Log.d(tag, "[String Pool] Size: " + stringPool.size());
        Log.d(tag, "[String Pool] " + stringPool);
    }

    /**
     * Resource IDs of Attributes
     * Each ID of int32
     * TODO: Use this information for XML generation.
     *
     * @param rmBuf
     * @param header_size
     * @param chunk_size
     * @throws Exception
     */
    private void parseResMapping(byte[] rmBuf, int header_size, int chunk_size) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(rmBuf);
        // Each ID of 4 bytes
        int num_of_res_ids = rmBuf.length / 4;

        byte[] int_buf = new byte[4];
        for (int i = 0; i < num_of_res_ids; i++) {
            read(in, int_buf);
            resMap.add(Utils.toInt(int_buf, false));
        }
        Log.d(tag, "[Res Mapping] Resource Mapping " + resMap);
    }

    /**
     * One namespace includes multiple XML elements
     * [Chunk_type] [Header_Size]
     * [Chunk Size]
     * <-- Chunk Body -->
     * [Line Number] - Line number where to place this
     * [Comment] - TODO: Skip it for the time being
     * [Prefix] - String pool index
     * [URI] - String Pool index
     *
     * @param nsStartBuf
     * @param header_size
     * @param chunk_size
     * @throws Exception
     */
    private void parseStartNameSpace(byte[] nsStartBuf, int header_size, int chunk_size) throws Exception {
        nodeIndex = 0;

        ByteArrayInputStream in = new ByteArrayInputStream(nsStartBuf);

        byte[] int_buf = new byte[4];
        read(in, int_buf);
        ns_linenumber = Utils.toInt(int_buf, false);

        read(in, int_buf);
        int comment = Utils.toInt(int_buf, false);

        read(in, int_buf);
        ns_prefix_index = Utils.toInt(int_buf, false);

        read(in, int_buf);
        ns_uri_index = Utils.toInt(int_buf, false);


        namespaceLookup.put(stringPool.get(ns_uri_index), stringPool.get(ns_prefix_index));
        Log.d(tag, "[Namespace Start]Line Number: " + ns_linenumber + " Prefix: " + stringPool.get(ns_prefix_index) + " URI: " + stringPool.get(ns_uri_index));
    }

    /**
     * XML_Start_Node Chunk Body-
     * line number- init32
     * comment- init32
     * ns - int32
     * name - int32
     * attributeStart- int16
     * attributeSize- int16
     * attributeCount - int16
     * id_index - int16
     * class_index - int16
     * style_index - int16
     * <p>
     * [Attributes]
     * ns- int32
     * name- int32
     * rawValue- init32
     * typedValue-
     * size- init16
     * res- init8
     * dataType- init8
     * data- init32
     * <p>
     * TODO: Retrieve Attribute data from resources.arsc
     *
     * @param xmlStartBuf
     * @param header_size
     * @param chunk_size
     * @throws Exception
     */
    private void parseXMLStart(byte[] xmlStartBuf, int header_size, int chunk_size) throws Exception {
        nodeIndex++;
        Node node = new Node();
        node.setIndex(nodeIndex);

        ByteArrayInputStream in = new ByteArrayInputStream(xmlStartBuf);

        byte[] int_buf = new byte[4];

        read(in, int_buf);
        int lineNumber = Utils.toInt(int_buf, false);
        node.setLinenumber(lineNumber);

        read(in, int_buf);
        int comment = Utils.toInt(int_buf, false);

        read(in, int_buf);
        int ns_index = Utils.toInt(int_buf, false);

        read(in, int_buf);
        int name_index = Utils.toInt(int_buf, false);

        byte[] short_buf = new byte[2];

        read(in, short_buf);
        int attributeStart = Utils.toInt(short_buf, false);

        read(in, short_buf);
        int attributeSize = Utils.toInt(short_buf, false);

        read(in, short_buf);
        int attributeCount = Utils.toInt(short_buf, false);

        // Skip ID, Class and Style index
        in.skip(6);

        Log.d(tag, "[XML Node] Name: " + (name_index == -1 ? "-1" : stringPool.get(name_index)) + " Attr count: " + attributeCount);

        if (name_index != -1) {
            node.setName(stringPool.get(name_index));

            if (ns_prefix_index != -1 && ns_uri_index != -1) {
                node.setNamespacePrefix(stringPool.get(ns_prefix_index));
                node.setNamespaceURI(stringPool.get(ns_uri_index));
            }
        }

        if (attributeCount == 0) {
            // No Attributes defined
            if (listener != null)
                listener.startNode(node);
            return;
        }

        for (int i = 0; i < attributeCount; i++) {
            Attribute attr = new Attribute();

            // attr ns
            read(in, int_buf);
            int attr_ns_index = Utils.toInt(int_buf, false);

            // attr name
            read(in, int_buf);
            int attr_name_index = Utils.toInt(int_buf, false);

            // Raw value. If user has directly mentioned value e.g. android:value="1". Reference to String Pool
            read(in, int_buf);
            int attr_raw_value = Utils.toInt(int_buf, false);

            String attr_value = "";

            if (attr_raw_value == -1) {
                // No Raw value defined.
                // Read Typed Value. Reference to Resource table e.g. String.xml, Drawable etc.
				/*
				 * Size of Types value- init16
				 * Res- init8 (Always 0)
				 * Data Type- init8
				 * Data- init32. Interpreted according to dataType
				 */
                read(in, short_buf);
                int data_size = Utils.toInt(short_buf, false);

                // Skip res value- Always 0
                in.skip(1);

                // TODO: Retrieve data based on Data_Type. Read Resource table.
                int data_type = in.read();

                read(in, int_buf);
                int data = Utils.toInt(int_buf, false); // Refer to Resource Table
                attr_value = "" + data;
                //Log.d(tag, "[Attribute] Value: "+ data);
            } else {
                attr_value = stringPool.get(attr_raw_value);
                //Log.d(tag, "[Attribute] Value: "+ attr_value);
                // Skip Typed value bytes
                in.skip(8);
            }

            if (attr_name_index != -1) {
                attr.setName(stringPool.get(attr_name_index));
                attr.setValue(attr_value);
                attr.setIndex(i);

                if (attr_ns_index != -1) {
                    final String key = stringPool.get(attr_ns_index);
                    attr.setNamespace(namespaceLookup.get(key));
                }

                node.addAttribute(attr);
            }

//			Log.d(tag, "[Attribute] NameSpace: "+ (attr_ns_index == -1 ? "-1": stringPool.get(attr_ns_index)) +
//								" Name: "+ (attr_name_index == -1 ? "-1" : stringPool.get(attr_name_index)));
        }

        if (listener != null) {
            listener.startNode(node);
        }

    }

    /**
     * XML_END Node Chunk Body-
     * [Line Number]
     * [Comment]
     * [Name space] - Name space. Ref to String pool, unless -1.
     * [Name] - Ref to String pool
     *
     * @param xmlEndBuf
     * @param header_size
     * @param chunk_size
     * @throws Exception
     */
    private void parseXMLEnd(byte[] xmlEndBuf, int header_size, int chunk_size) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(xmlEndBuf);

        byte[] int_buf = new byte[4];
        read(in, int_buf);
        int lineNumber = Utils.toInt(int_buf, false);

        read(in, int_buf);
        int comment = Utils.toInt(int_buf, false);

        read(in, int_buf);
        int ns_index = Utils.toInt(int_buf, false);

        read(in, int_buf);
        int name_index = Utils.toInt(int_buf, false);

        Log.d(tag, "[XML_END] Line Number: " + lineNumber + " Namespace: " + ns_index + " Name: " + (name_index == -1 ? "-1" : stringPool.get(name_index)));

        if (name_index != -1) {
            Node node = new Node();
            node.setName(stringPool.get(name_index));
            node.setLinenumber(lineNumber);
            node.setNamespacePrefix(stringPool.get(ns_prefix_index));
            node.setNamespaceURI(stringPool.get(ns_uri_index));

            if (listener != null)
                listener.endNode(node);
        }
    }

    /**
     * End of Name space. Chunk structure same as Start of Name space
     *
     * @param nsStartBuf
     * @param header_size
     * @param chunk_size
     * @throws Exception
     */
    private void parseEndNameSpace(byte[] nsStartBuf, int header_size, int chunk_size) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(nsStartBuf);

        byte[] int_buf = new byte[4];
        read(in, int_buf);
        int lineNumber = Utils.toInt(int_buf, false);

        read(in, int_buf);
        int comment = Utils.toInt(int_buf, false);

        read(in, int_buf);
        int prefix_index = Utils.toInt(int_buf, false);

        read(in, int_buf);
        int uri_index = Utils.toInt(int_buf, false);

        Log.d(tag, "[Namespace END]Line Number: " + lineNumber + " Prefix: " + prefix_index + " URI: " + uri_index);
    }

    /**
     * Parse resources.arsc (Resource Table).
     * StringPool and Resource data collected from this File will be used for
     * Binary XML layout files.
     * <p>
     * ## Ref
     * http://justanapplication.wordpress.com/category/android/android-resources/
     *
     * @param arscFile
     * @throws Exception
     */
    public void parseResourceTable(String arscFile) throws Exception {
        // 1. Parse Resource header
        // 2. Parse Resource string pool
        // 3. Parse Resource pacakges.

        // Clear
        stringPool.clear();

        Log.d(tag, "[Res_Table] File: " + arscFile);


        BufferedInputStream in = new BufferedInputStream(new FileInputStream(arscFile));

        // Is it an valid BXML ?
		/*
		 * Chunk header meta size - 8 bytes
		 * [Chunk Type] - 2 bytes
		 * [Chunk Header Size] - 2 bytes
		 * [Chunk Size] - 4 bytes
		 */

        // Chunk type- 2 bytes
        read(in, buf_2);
        Log.d(tag, "[Res_Table] Chunk type: " + Utils.toInt(buf_2, false));

        if (Utils.toInt(buf_2, false) != RES_TABLE_TYPE) {
            Log.d(tag, "It's an invalid Resources.arsc file. Exiting!");
            return;
        }

        // Header size- 2 bytes
        read(in, buf_2);
        header_size = Utils.toInt(buf_2, false);

        // Chunk size- 4 bytes
        read(in, buf_4);
        chunk_size = Utils.toInt(buf_4, false);

        // Package count- 4 bytes
        read(in, buf_4);
        package_count = Utils.toInt(buf_4, false);

        Log.d(tag, "[Res_Table] Header Size: " + header_size + " Chunk size: " + chunk_size + " Package_count: " + package_count);

		/*
		 * String Pool
		 */
        // Read next chunk- 2 bytes
        read(in, buf_2);

        Log.d(tag, "[Res_Table] Chunk type: " + Utils.toInt(buf_2, false) + " -->" + buf_2[0] + " " + buf_2[1]);

        if (Utils.toInt(buf_2, false) == RES_STRING_POOL_TYPE) // String Pool
        {
            // String Pool/Tokens
            Log.d(tag, "String Pool...");
            read(in, buf_2);
            header_size = Utils.toInt(buf_2, false);

            read(in, buf_4);
            chunk_size = Utils.toInt(buf_4, false);

            Log.d(tag, "String Pool...Header Size: " + header_size + " Chunk Size: " + chunk_size);

            byte[] spBuf = new byte[chunk_size - 8];
            read(in, spBuf);

            // Parse String pool
            parseStringPool(spBuf, header_size, chunk_size);

            // Get the next Chunk
            read(in, buf_2);
        }

        // TODO: Parse Resource package
        Log.d(tag, "[Res_Table] Chunk type: " + Utils.toInt(buf_2, false));

        if (Utils.toInt(buf_2, false) == RES_TABLE_PACKAGE_TYPE) // RES_Table_Package
        {
            // Parse Resource package stream
            parseResPackage(in);
        }

        Log.d(tag, "Resource.arsc parsing done!!");
    }

    /**
     * Resource Package chunk contains-
     * Package Header
     * Type String Pool
     * Key String pool
     * Type1 typespec chunk
     * Type1 type chunk
     * ....
     * ....
     * TypeN type spec chunk
     * TypeN type chunk
     *
     * @param resPackageBuf
     * @throws Exception
     */
    private void parseResPackage(BufferedInputStream in) throws Exception {
        // Header size- 2 bytes
        read(in, buf_2);
        header_size = Utils.toInt(buf_2, false);

        // Chunk size- 4 bytes
        read(in, buf_4);
        chunk_size = Utils.toInt(buf_4, false);

        read(in, buf_4);
        int packg_id = Utils.toInt(buf_4, false);

        Log.d(tag, "String Pool...Header Size: " + header_size
                + " Chunk Size: " + chunk_size + " Packg_ID: " + packg_id);

        // 128 Characters (16-bit Char)
        byte[] packg_name_buf = new byte[256];
        read(in, packg_name_buf);

        String packg_name = Utils.toString(packg_name_buf, false);
        Log.d(tag, "Package Name: " + new String(packg_name));

        // typeStrings- init32
        // Index/Offset position of Type String Pool
        read(in, buf_4);
        int typeStrings = Utils.toInt(buf_4, false);

        // Last public type
        // Index (from end) or Count of Types defined in Type String Pool (last lastPublicType entries)
        read(in, buf_4);
        int lastPublicType = Utils.toInt(buf_4, false);

        // Key String
        // Index/Offset position of Key String Pool
        read(in, buf_4);
        int keyString = Utils.toInt(buf_4, false);

        // Last index into Key string
        // Index (from end) or Count of Keys defined in Key String Pool (last lastPublicKey entries)
        read(in, buf_4);
        int lastPublicKey = Utils.toInt(buf_4, false);

        Log.d(tag, "[Res_Table] typeStrings=" + typeStrings
                + " lastPublicType=" + lastPublicType + " keyString="
                + keyString + " lastPublicKey=" + lastPublicKey);

        // Parse "Type String Pool"
        read(in, buf_2);
        if (Utils.toInt(buf_2, false) == RES_STRING_POOL_TYPE) {
            // String Pool/Tokens
            Log.d(tag, "String Pool...");
            read(in, buf_2);
            header_size = Utils.toInt(buf_2, false);

            read(in, buf_4);
            chunk_size = Utils.toInt(buf_4, false);

            Log.d(tag, "String Pool...Header Size: " + header_size
                    + " Chunk Size: " + chunk_size);

            byte[] spBuf = new byte[chunk_size - 8];
            read(in, spBuf);

            // Parse String pool
            parseStringPool(spBuf, header_size, chunk_size);

            // Get the next Chunk
            read(in, buf_2);
        }

        // Parse "Key String Pool"
        if (Utils.toInt(buf_2, false) == RES_STRING_POOL_TYPE) {
            // String Pool/Tokens
            Log.d(tag, "String Pool...");
            read(in, buf_2);
            header_size = Utils.toInt(buf_2, false);

            read(in, buf_4);
            chunk_size = Utils.toInt(buf_4, false);

            Log.d(tag, "String Pool...Header Size: " + header_size
                    + " Chunk Size: " + chunk_size);

            byte[] spBuf = new byte[chunk_size - 8];
            read(in, spBuf);

            // Parse String pool
            parseStringPool(spBuf, header_size, chunk_size);

            // Get the next Chunk
            read(in, buf_2);
        }

        // TODO: Res_Type, Res_Type_Spec
    }

    /**
     * ResTable_type
     */
    private void parseResType() {

    }

    /**
     * ResTable_typeSpec
     */
    private void parseResTypeSpec() {

    }

    private void read(final InputStream in, final byte[] buffer) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        in.read(buffer);
    }
}