package de.thm.dbiGenerator.;
			
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("accessDenied")
public class AccessDeniedView extends VerticalLayout {
    AccessDeniedView() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(new Label("Access denied!"));
        add(formLayout);
    }
}
