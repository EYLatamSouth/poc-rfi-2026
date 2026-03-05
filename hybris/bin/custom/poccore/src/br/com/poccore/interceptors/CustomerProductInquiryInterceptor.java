package br.com.poccore.interceptors;

import br.com.poccore.model.CustomerProductInquiryModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;

import static br.com.poccore.constants.PoccoreConstants.ROLE_SUPPORT;

public class CustomerProductInquiryInterceptor implements ValidateInterceptor<CustomerProductInquiryModel> {

    private UserService userService;

    @Override
    public void onValidate(CustomerProductInquiryModel customerProductInquiryModel, InterceptorContext ctx) throws InterceptorException {
        if(!ctx.isNew(customerProductInquiryModel) && ctx.isModified(customerProductInquiryModel, CustomerProductInquiryModel.ANSWER)) {
            if(canUserModifyAnswer()) {
                customerProductInquiryModel.setAnswerDate(new Date());
            } else {
                throw new InterceptorException("User don't have permission to answer this inquiry");
            }
        }
    }

    private boolean canUserModifyAnswer() {
        UserModel userModel = getUserService().getCurrentUser();
        UserGroupModel userGroupModel = getUserService().getUserGroupForUID(ROLE_SUPPORT);

        return getUserService().isAdmin(userModel) || getUserService().isAdminEmployee(userModel) || getUserService().isMemberOfGroup(userModel, userGroupModel);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
