package gr.nlamp.sfgame_backend.initialization;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

@Component
public class BigDataLoader {

    public static BigInteger[] loadBigNumbersFromXML(String filePath) {
        BigInteger[] bigNumbers = null;  // Start with a null array.
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodes = document.getElementsByTagName("number");
            bigNumbers = new BigInteger[nodes.getLength()];  // Initialize the array based on the number of nodes.

            for (int i = 0; i < nodes.getLength(); i++) {
                String numText = nodes.item(i).getTextContent().trim();
                // Convert to BigDecimal first to handle scientific notation.
                BigDecimal bigDecimalValue = new BigDecimal(numText);
                // Convert BigDecimal to BigInteger (this drops the decimal part).
                bigNumbers[i] = bigDecimalValue.toBigInteger();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bigNumbers;
    }

}
