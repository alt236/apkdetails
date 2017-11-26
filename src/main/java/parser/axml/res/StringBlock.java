/*
 * Copyright 2008 Android4ME 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package parser.axml.res;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author Dmitry Skiba
 * <p>
 * Block of strings, used in binary xml and arsc.
 * <p>
 * TODO:
 * - implement get()
 */
public class StringBlock {

    private static final int UTF8_FLAG = 0x100;
    private static final int CHUNK_TYPE = 0x001C0001;
    private int[] m_stringOffsets;
    private byte[] m_strings;
    private int[] m_styleOffsets;
    private int[] m_styles;

    ///////////////////////////////////////////// implementation 
    private boolean m_isUTF8;

    /**
     * Reads whole (including chunk type) string block from stream.
     * Stream must be at the chunk type.
     */
    public StringBlock(IntReader reader) throws IOException {
        int resType = reader.readInt();
        if (resType != CHUNK_TYPE)
            throw new IOException("Expected chunk of type 0x" + Integer.toHexString(CHUNK_TYPE) +
                    ", read 0x" + Integer.toHexString(resType) + ".");
        int chunkSize = reader.readInt();
        int stringCount = reader.readInt();
        int styleOffsetCount = reader.readInt();
        int flags = reader.readInt();
        int stringsOffset = reader.readInt();
        int stylesOffset = reader.readInt();
        m_isUTF8 = (flags & UTF8_FLAG) != 0;
        m_stringOffsets = reader.readIntArray(stringCount);
        if (styleOffsetCount != 0) {
            m_styleOffsets = reader.readIntArray(styleOffsetCount);
        }
        {
            int size = ((stylesOffset == 0) ? chunkSize : stylesOffset) - stringsOffset;
            if ((size % 4) != 0) {
                throw new IOException("String data size is not multiple of 4 (" + size + ").");
            }
            m_strings = reader.readByteArray(size);
        }
        if (stylesOffset != 0) {
            int size = (chunkSize - stylesOffset);
            if ((size % 4) != 0) {
                throw new IOException("Style data size is not multiple of 4 (" + size + ").");
            }
            m_styles = reader.readIntArray(size / 4);
        }

    }

    /**
     * Returns number of strings in block.
     */
    public int getCount() {
        return m_stringOffsets != null ?
                m_stringOffsets.length :
                0;
    }

    /**
     * Returns raw string (without any styling information) at specified index.
     * Returns null if index is invalid or object was not initialized.
     */
    public String getString(int index) {
        if (index < 0
                || m_stringOffsets == null
                || index >= m_stringOffsets.length) {
            return null;
        }
        int offset = m_stringOffsets[index];
        int length;
        if (m_isUTF8) {
            offset += getVarint(m_strings, offset)[1];
            int[] varint = getVarint(m_strings, offset);
            offset += varint[1];
            length = varint[0];
            byte[] result = new byte[length];
            System.arraycopy(m_strings, offset, result, 0, length);
            try {
                return new String(result, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            length = getShort(m_strings, offset);
            StringBuilder result = new StringBuilder(length);
            for (; length != 0; length -= 1) {
                offset += 2;
                result.append((char) getShort(m_strings, offset));
            }
            return result.toString();
        }
        return null;
    }

    private final int[] getVarint(byte[] array, int offset) {
        int val = array[offset];
        boolean more = (val & 0x80) != 0;
        val &= 0x7f;

        if (!more) {
            return new int[]{val, 1};
        } else {
            return new int[]{val << 8 | array[offset + 1] & 0xff, 2};
        }
    }

    /**
     * Returns string with style tags (html-like).
     */
    public String getHTML(int index) {
        String raw = getString(index);
        if (raw == null) {
            return raw;
        }
        int[] style = getStyle(index);
        if (style == null) {
            return raw;
        }
        StringBuilder html = new StringBuilder(raw.length() + 32);
        int offset = 0;
        while (true) {
            int i = -1;
            for (int j = 0; j != style.length; j += 3) {
                if (style[j + 1] == -1) {
                    continue;
                }
                if (i == -1 || style[i + 1] > style[j + 1]) {
                    i = j;
                }
            }
            int start = ((i != -1) ? style[i + 1] : raw.length());
            for (int j = 0; j != style.length; j += 3) {
                int end = style[j + 2];
                if (end == -1 || end >= start) {
                    continue;
                }
                if (offset <= end) {
                    html.append(raw, offset, end + 1);
                    offset = end + 1;
                }
                style[j + 2] = -1;
                html.append('<');
                html.append('/');
                html.append(getString(style[j]));
                html.append('>');
            }
            if (offset < start) {
                html.append(raw, offset, start);
                offset = start;
            }
            if (i == -1) {
                break;
            }
            html.append('<');
            html.append(getString(style[i]));
            html.append('>');
            style[i + 1] = -1;
        }
        return html.toString();
    }

    /**
     * Finds index of the string.
     * Returns -1 if the string was not found.
     */
    public int find(String string) {
        if (string == null) {
            return -1;
        }
        for (int i = 0; i != m_stringOffsets.length; ++i) {
            int offset = m_stringOffsets[i];
            int length = getShort(m_strings, offset);
            if (length != string.length()) {
                continue;
            }
            int j = 0;
            for (; j != length; ++j) {
                offset += 2;
                if (string.charAt(j) != getShort(m_strings, offset)) {
                    break;
                }
            }
            if (j == length) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns style information - array of int triplets,
     * where in each triplet:
     * * first int is index of tag name ('b','i', etc.)
     * * second int is tag start index in string
     * * third int is tag end index in string
     */
    private int[] getStyle(int index) {
        if (m_styleOffsets == null || m_styles == null ||
                index >= m_styleOffsets.length) {
            return null;
        }
        int offset = m_styleOffsets[index] / 4;
        int style[];
        int count = 0;
        for (int i = offset; i < m_styles.length; ++i) {
            if (m_styles[i] == -1) {
                break;
            }
            count += 1;
        }
        if (count == 0 || (count % 3) != 0) {
            return null;
        }
        style = new int[count];
        for (int i = offset, j = 0; i < m_styles.length; ) {
            if (m_styles[i] == -1) {
                break;
            }
            style[j++] = m_styles[i++];
        }
        return style;
    }

    private final int getShort(byte[] array, int offset) {
        return (array[offset + 1] & 0xff) << 8 | array[offset] & 0xff;
    }
}