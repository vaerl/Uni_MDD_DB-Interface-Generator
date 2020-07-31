package de.thm.dbiGenerator.editors;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.binder.Binder;
import org.vaadin.gatanaso.MultiselectComboBox;
import com.vaadin.flow.spring.annotation.UIScope;
import de.thm.dbiGenerator.entities.Team;
import de.thm.dbiGenerator.entities.Game;
import de.thm.dbiGenerator.repositories.TeamRepository;
import de.thm.dbiGenerator.repositories.GameRepository;
import de.thm.dbiGenerator.ChangeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Component
@UIScope
public class TeamEditor extends Dialog implements KeyNotifier {

    private TeamRepository teamRepository;
    private GameRepository gameRepository;
    private ChangeHandler changeHandler;
    private Team team;

    //buttons
    Button save = new Button("Speichern", VaadinIcon.CHECK.create());
    Button cancel = new Button("Abbrechen");
    Button delete = new Button("Löschen", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    //fields to edit
    TextField name = new TextField("Name");
    IntegerField points = new IntegerField("Points");
	Select<Team.Status> status = new Select<>();
	Select<Team.Gender> gender = new Select<>();
	MultiselectComboBox<Game> multiselectComboBoxGame = new MultiselectComboBox();
	VerticalLayout fields = new VerticalLayout(
	name, points
	, 
	status, gender
	, 
	multiselectComboBoxGame
	);
	Binder<Team> binder = new Binder<>(Team.class);

    @Autowired
    public TeamEditor(
    	 TeamRepository teamRepository,
    	 GameRepository gameRepository
    ) {
    	
    	super();
    	   this.teamRepository = teamRepository;
    	   this.gameRepository = gameRepository;
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
        multiselectComboBoxGame.setWidth("100%");
        multiselectComboBoxGame.setLabel("Games");
        multiselectComboBoxGame.setPlaceholder("Choose...");
        List<Game> gameList = getGames();
        multiselectComboBoxGame.setItemLabelGenerator(Game::getName);
        multiselectComboBoxGame.setItems(gameList);
        multiselectComboBoxGame.setRequired(true); // mark as mandatory
        multiselectComboBoxGame.setErrorMessage("This field is required"); // set error message
        
        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    public final void edit(Team team) {
        if (team == null) {
            close();
            return;
        }

        final boolean persisted = team.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            this.team = teamRepository.findById(team.getId()).get();
        }
        else {
            this.team = team;
        }

        this.binder.setBean(this.team);
        open();
        this.name.focus();
    }

    void save() {
        if (this.team.getStatus() == null || this.team.getName() == null || this.team.getPoints() == null || this.team.getGender() == null
         || 
        this.team.getGames() == null
        ){
            return;
        }
        teamRepository.save(this.team);
        this.changeHandler.onChange();
    }

	void delete() {
	      teamRepository.delete(this.team);
	      this.changeHandler.onChange();
	  }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete is clicked
        this.changeHandler = h;
    }
    
	public List<Game> getGames() {
		return gameRepository.findAll();
	}

}
