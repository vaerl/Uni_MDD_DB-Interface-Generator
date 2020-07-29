package de.thm.dbiGenerator;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import de.thm.dbiGenerator.pages.TeamGridPage;
import de.thm.dbiGenerator.pages.GameGridPage;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Route("")
@RouteAlias("main")
@UIScope
@Component
public class MainView extends VerticalLayout {
	
	private HttpServletRequest request;
	
	@Autowired
	    MainView(HttpServletRequest request, TeamGridPage TeamPage, GameGridPage GamePage) {
	    	super();
	    	this.request = request;
	    	
	    	Button logout = new Button(VaadinIcon.SIGN_OUT.create());
	    	logout.getStyle().set("font-size", "48px");
	    	logout.setHeight("96px");
	    	logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	    	logout.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
	    		requestLogout();
	    	});
	    	
	    	HorizontalLayout logoutWrapper = new HorizontalLayout();
	    	logoutWrapper.add(logout);
	    	logoutWrapper.setWidth("8%");
	    	logoutWrapper.setJustifyContentMode(JustifyContentMode.END);
	    	Tab Team = new Tab("Team");
	    	        Team.getStyle().set("font-size", "48px");
	    	Tab Game = new Tab("Game");
	    	        Game.getStyle().set("font-size", "48px");
	    	Tabs tabs = new Tabs(Team, Game);
	    	tabs.setSelectedTab(Team);
	    	tabs.setFlexGrowForEnclosedTabs(1);
	    	tabs.setWidthFull();
	    	
	    	HorizontalLayout tabWrapper = new HorizontalLayout(tabs);
	    	tabWrapper.setWidth("92%");
	    	tabWrapper.setJustifyContentMode(JustifyContentMode.START);
	    	
	    	HorizontalLayout bar = new HorizontalLayout(tabWrapper, logoutWrapper);
	    	bar.setWidthFull();
	    	
	    	Map<Tab, VerticalLayout> tabsToPages = new HashMap<>();
	    	tabsToPages.put(Team, TeamPage);
	    	tabsToPages.put(Game, GamePage);
	    	
	    	tabs.addSelectedChangeListener(event -> {
	    	     removeAll();
	    	     add(bar, tabsToPages.get(tabs.getSelectedTab()));
	    	});
	    	
	    	setSizeFull();
	    	add(bar);
	    	
	    	UI.getCurrent().addBeforeEnterListener((BeforeEnterListener) beforeEnterEvent -> {
	    	     if (beforeEnterEvent.getNavigationTarget() != AccessDeniedView.class && // This is to avoid a
	    	     // loop if DeniedAccessView is the target
	    	     !this.request.isUserInRole("ADMIN")) {
	    	     	beforeEnterEvent.rerouteTo(AccessDeniedView.class);
	    	     }
	    	});
}

void requestLogout() {
        //https://stackoverflow.com/a/5727444/1572286
        SecurityContextHolder.clearContext();
        request.getSession(false).invalidate();

        // And this is similar to how logout is handled in Vaadin 8:
        // https://vaadin.com/docs/v8/framework/articles/HandlingLogout.html
        UI.getCurrent().getSession().close();
        UI.getCurrent().getPage().reload();// to redirect user to the login page
    }
}
