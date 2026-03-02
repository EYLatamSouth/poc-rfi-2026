import de.hybris.platform.servicelayer.search.*
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.core.model.user.CustomerModel;
import br.com.poccore.model.CustomerProductInquiryModel;

def flexibleSearchService = spring.getBean("flexibleSearchService")
def modelService = spring.getBean("modelService")

def queryCustomer = new FlexibleSearchQuery("SELECT {pk} FROM {Customer}")

def searchResultCustomer = flexibleSearchService.search(queryCustomer)
def customers = searchResultCustomer.getResult()

customers.each { c ->
    println("Customer PK: ${c.getPk()} UID: ${c.getUid()}");

    def queryInquiry = new FlexibleSearchQuery("SELECT {pk} FROM {CustomerProductInquiry} where {customer} = " + c.pk);
    def searchResultInquiry = flexibleSearchService.search(queryInquiry);
    def inquiries = searchResultInquiry.getResult();
    def customerInquiries = new HashSet();

    inquiries.each { i ->
        customerInquiries.add(i);
    };

    println("Inquiry list: " + customerInquiries);
    c.setCustomerProductInquiries(customerInquiries);
    modelService.save(c);
}
