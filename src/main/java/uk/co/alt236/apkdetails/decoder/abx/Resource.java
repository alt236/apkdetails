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

public interface Resource {
    // Resource Types
    int RES_NULL_TYPE = 0x0000,
            RES_STRING_POOL_TYPE = 0x0001,
            RES_TABLE_TYPE = 0x0002,
            RES_XML_TYPE = 0x0003,

    // Chunk types in RES_XML_TYPE
    RES_XML_FIRST_CHUNK_TYPE = 0x0100,
            RES_XML_START_NAMESPACE_TYPE = 0x0100,
            RES_XML_END_NAMESPACE_TYPE = 0x0101,
            RES_XML_START_ELEMENT_TYPE = 0x0102,
            RES_XML_END_ELEMENT_TYPE = 0x0103,
            RES_XML_CDATA_TYPE = 0x0104,
            RES_XML_LAST_CHUNK_TYPE = 0x017f,

    // This contains a uint32_t array mapping strings in the string
    // pool back to resource identifiers. It is optional.
    RES_XML_RESOURCE_MAP_TYPE = 0x0180,

    // Chunk types in RES_TABLE_TYPE
    RES_TABLE_PACKAGE_TYPE = 0x0200,
            RES_TABLE_TYPE_TYPE = 0x0201,
            RES_TABLE_TYPE_SPEC_TYPE = 0x0202;
}
