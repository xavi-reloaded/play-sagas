package actions;

import play.mvc.With;

import java.lang.annotation.*;

@With(AdminAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface IAdminAction
{

}