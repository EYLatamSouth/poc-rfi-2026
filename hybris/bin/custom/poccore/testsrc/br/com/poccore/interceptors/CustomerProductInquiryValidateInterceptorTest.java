package br.com.poccore.interceptors;

import br.com.poccore.model.CustomerProductInquiryModel;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static br.com.poccore.constants.PoccoreConstants.ROLE_SUPPORT;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomerProductInquiryValidateInterceptorTest {

    @InjectMocks
    private CustomerProductInquiryValidateInterceptor customerProductInquiryInterceptor;

    @Mock
    private UserService userService;

    @Mock
    private InterceptorContext ctx;

    @Before
    public void setUp() {
        customerProductInquiryInterceptor.setUserService(userService);
    }

    @Test
    public void testNewInquiryShouldNotSetAnswerDate() throws InterceptorException {
        CustomerProductInquiryModel model = new CustomerProductInquiryModel();
        when(ctx.isNew(model)).thenReturn(true);

        customerProductInquiryInterceptor.onValidate(model, ctx);

        assertNull(model.getAnswerDate());
        verify(userService, never()).getCurrentUser();
        verify(userService, never()).getUserGroupForUID(anyString());
    }

    @Test
    public void testInquiryNotChangeAnswerShouldNotSetAnswerDate() throws InterceptorException {
        CustomerProductInquiryModel model = new CustomerProductInquiryModel();
        when(ctx.isNew(model)).thenReturn(false);
        when(ctx.isModified(model, CustomerProductInquiryModel.ANSWER)).thenReturn(false);

        customerProductInquiryInterceptor.onValidate(model, ctx);

        assertNull(model.getAnswerDate());
        verify(userService, never()).getCurrentUser();
        verify(userService, never()).getUserGroupForUID(anyString());
    }

    @Test
    public void testWhenUserDontHavePermissionToChangeAnswer() throws InterceptorException {
        CustomerProductInquiryModel model = new CustomerProductInquiryModel();
        setCommonsMocks(model, false, false, false);

        assertThrows(InterceptorException.class, () -> customerProductInquiryInterceptor.onValidate(model, ctx));
        assertNull(model.getAnswerDate());
        verify(userService, times(1)).getCurrentUser();
        verify(userService, times(1)).getUserGroupForUID(ROLE_SUPPORT);
        verify(userService, times(1)).isAdmin(any());
        verify(userService, times(1)).isAdminEmployee(any());
        verify(userService, times(1)).isMemberOfGroup(any(UserModel.class), any(UserGroupModel.class));
    }

    @Test
    public void testWhenUserIsAdmin() throws InterceptorException {
        CustomerProductInquiryModel model = new CustomerProductInquiryModel();
        setCommonsMocks(model, true, false, false);
        customerProductInquiryInterceptor.onValidate(model, ctx);

        assertNotNull(model.getAnswerDate());
    }

    @Test
    public void testWhenUserIsEmployeeAdmin() throws InterceptorException {
        CustomerProductInquiryModel model = new CustomerProductInquiryModel();
        setCommonsMocks(model, false, true, false);
        customerProductInquiryInterceptor.onValidate(model, ctx);

        assertNotNull(model.getAnswerDate());
    }

    @Test
    public void testWhenUserIsSupport() throws InterceptorException {
        CustomerProductInquiryModel model = new CustomerProductInquiryModel();
        setCommonsMocks(model, false, false, true);
        customerProductInquiryInterceptor.onValidate(model, ctx);

        assertNotNull(model.getAnswerDate());
    }

    private void setCommonsMocks(CustomerProductInquiryModel model, Boolean isAdmin, Boolean isAdminEmployee, Boolean isMemberOfGroup) {
        when(ctx.isNew(model)).thenReturn(false);
        when(ctx.isModified(model, CustomerProductInquiryModel.ANSWER)).thenReturn(true);

        UserModel userModel = mock(UserModel.class);
        when(userService.getCurrentUser()).thenReturn(userModel);
        UserGroupModel userGroupModel = mock(UserGroupModel.class);
        when(userService.getUserGroupForUID(ROLE_SUPPORT)).thenReturn(userGroupModel);

        when(userService.isAdmin(userModel)).thenReturn(isAdmin);
        when(userService.isAdminEmployee(userModel)).thenReturn(isAdminEmployee);
        when(userService.isMemberOfGroup(userModel, userGroupModel)).thenReturn(isMemberOfGroup);
    }


}
