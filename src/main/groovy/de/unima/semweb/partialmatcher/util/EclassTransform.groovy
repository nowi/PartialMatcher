package de.unima.semweb.partialmatcher.util

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants

/**
 * User: nowi
 * Date: 19.05.2008
 * Time: 20:12:015
 */

/**
 * Groovy script for transformation of the Pepperl and Fuchs Eclass Catalog to EclassOWL
 * Date: 19.05.2008
 * Time: 19:24:59
 */
class DataType {
    def name
    def value
    def unit

    @Override
    boolean equals(Object p) {
        return name.equals(p.name)
    }

    @Override
    int hashCode() {
        return name.hashCode()
    }

    @Override
    String toString() {
        "[$name , $value , $unit]"
    }

}


File pathToCatalog = new File("/Users/nowi/workspace/SemanticWeb/input/catalog.xml")

// slurp in the xml file
builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
def catalog = builder.parse(pathToCatalog).documentElement

def xpath = XPathFactory.newInstance().newXPath()


//Set<DataType> dataTypes = new HashSet<DataType>()
// seek features with associated units and add them into a unique set

Set<DataType> dataTypes = xpath.evaluate('//FEATURE', catalog, XPathConstants.NODESET).collect {feature ->
    def name = xpath.evaluate('./FNAME', feature)
    def value = xpath.evaluate('./FVALUE', feature)
    def unit = xpath.evaluate('./FUNIT', feature)
    if (name && value && unit) {
        DataType property = new DataType(name: name, value: value, unit: unit)
        if(!properties.contains(property)){
            println "adding : $property"
            property
        }
    }

}

println properties
println properties.size()
// scan all FNAME VALUE PAIRS AND STORE IN A MAP



