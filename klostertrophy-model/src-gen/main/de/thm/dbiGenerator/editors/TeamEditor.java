package de.thm.dbiGenerator.editors;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import de.thm.dbiGenerator.entities.Team;
import de.thm.dbiGenerator.repos.TeamRepository;
import de.thm.dbiGenerator.ChangeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumSet;

@Component
@UIScope
public class TeamEditor extends Dialog implements KeyNotifier {

    private TeamRepository TeamRepository;
    private ChangeHandler changeHandler;
    private Team game;

    //buttons
    Button save = new Button("Speichern", VaadinIcon.CHECK.create());
    Button cancel = new Button("Abbrechen");
    Button delete = new Button("Löschen", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    //fields to edit
    TextField name = new TextField("Team-Name");
	Select<Team.Status> status = new Select<>();
	Select<Team.Gender> gender = new Select<>();
	HorizontalLayout fields = new HorizontalLayout(name, status, gender);
	Binder<Team> binder = new Binder<>(Team.class);

    @Autowired
    public TeamEditor(TeamRepository TeamRepository) {
	   	super();
   	    this.TeamRepository = TeamRepository;
   	    add(fields, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        //actions
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        addKeyPressListener(Key.ENTER, e -> save());
        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> changeHandler.onChange());

        //fields
        status.setLabel("Status");
        status.setItemLabelGenerator(Team.Status::toString);
        status.setItems(new ArrayList<>(EnumSet.allOf(Team.Status.class)));
        gender.setLabel("Gender");
        gender.setItemLabelGenerator(Team.Gender::toString);
        gender.setItems(new ArrayList<>(EnumSet.allOf(Team.Gender.class)));
        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    public final void edit(Team Team) {
        if (Team == null) {
            close();
            return;
        }

        final boolean persisted = Team.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            this.Team = TeamRepository.findById(Team.getId()).get();
        }
        else {
            this.Team = Team;
        }

        this.binder.setBean(this.Team);
        open();
        this.name.focus();
    }

    void save() {
        if (this.Team.getStatus() == null || this.Team.getGender() == null || this.Team.getName() == null){
            return;
        }
        TeamRepository.save(this.Team);
        this.changeHandler.onChange();
    }

	void delete() {
       TeamRepository.delete(this.Team);
       this.changeHandler.onChange();
   }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete is clicked
        this.changeHandler = h;
    }

}
