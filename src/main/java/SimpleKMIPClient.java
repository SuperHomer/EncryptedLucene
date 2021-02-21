import ch.ntb.inf.kmip.attributes.CryptographicAlgorithm;
import ch.ntb.inf.kmip.attributes.CryptographicLength;
import ch.ntb.inf.kmip.attributes.CryptographicUsageMask;
import ch.ntb.inf.kmip.attributes.ObjectType;
import ch.ntb.inf.kmip.container.KMIPBatch;
import ch.ntb.inf.kmip.container.KMIPContainer;
import ch.ntb.inf.kmip.kmipenum.EnumCryptographicAlgorithm;
import ch.ntb.inf.kmip.kmipenum.EnumObjectType;
import ch.ntb.inf.kmip.kmipenum.EnumOperation;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.objects.base.TemplateAttribute;
import ch.ntb.inf.kmip.objects.base.TemplateAttributeStructure;
//import ch.ntb.inf.kmip.stub.KMIPStub;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SimpleKMIPClient {

    // initialize Logger
    private static final Logger logger = Logger.getLogger(String.valueOf(SimpleKMIPClient.class));

    public static void main(String[] args) {
        // configure Logger
        //DOMConfigurator.configureAndWatch( "config/log4j-1.2.17.xml", 60*1000 );
        KMIPStub2 stub = new KMIPStub2();
        KMIPContainer request = createKMIPRequest();
        KMIPContainer response = stub.processRequest(request);
        System.out.println(response.toString());
    }

    private static KMIPContainer createKMIPRequest() {
        // Create Container with one Batch
        KMIPContainer container = new KMIPContainer();
        KMIPBatch batch = new KMIPBatch();
        container.addBatch(batch);
        container.calculateBatchCount();
        // Set Operation and Attribute
        batch.setOperation(EnumOperation.Create);
        batch.addAttribute(new ObjectType(EnumObjectType.SymmetricKey));
        // Set TemplateAttribute with Attributes
        ArrayList<Attribute> templateAttributes = new ArrayList<Attribute>();
        templateAttributes.add(new CryptographicAlgorithm(EnumCryptographicAlgorithm.AES));
        templateAttributes.add(new CryptographicLength(128));
        templateAttributes.add(new CryptographicUsageMask(0x0C));
        TemplateAttributeStructure tas = new TemplateAttribute();
        tas.setAttributes(templateAttributes);
        batch.addTemplateAttributeStructure(tas);

        return container;
    }
}