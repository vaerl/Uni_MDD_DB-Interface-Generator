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
import de.thm.dbiGenerator.entities.Game;
import de.thm.dbiGenerator.entities.Team;
import de.thm.dbiGenerator.repositories.GameRepository;
import de.thm.dbiGenerator.repositories.TeamRepository;
import de.thm.dbiGenerator.ChangeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

@Component
@UIScope
public class GameEditor extends Dialog implements KeyNotifier {

    private GameRepository gameRepository;
    private TeamRepository teamRepository;
    private ChangeHandler changeHandler;
    private Game game;

    //buttons
    Button save = new Button("Speichern", VaadinIcon.CHECK.create());
    Button cancel = new Button("Abbrechen");
    Button delete = new Button("Löschen", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    //fields to edit
    TextField name = new TextField("Name");
	Select<Game.Status> status = new Select<>();
	Select<Game.SortOrder> sortOrder = new Select<>();
	Select<Game.PointType> pointType = new Select<>();
	MultiselectComboBox<Team> multiselectComboBoxTeam = new MultiselectComboBox<>();
	VerticalLayout fields = new VerticalLayout(
	name
	, 
	status, sortOrder, pointType
	, 
	multiselectComboBoxTeam);
	Binder<Game> binder = new Binder<>(Game.class);

    @Autowired
    public GameEditor(
    	 GameRepository gameRepository,
    	 TeamRepository teamRepository
    ) {
    	
    	super();
    	   this.gameRepository = gameRepository;
    	   this.teamRepository = teamRepository;
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
        status.setItemLabelGenerator(Game.Status::toString);
        status.setItems(new ArrayList<>(EnumSet.allOf(Game.Status.class)));
        sortOrder.setLabel("SortOrder");
        sortOrder.setItemLabelGenerator(Game.SortOrder::toString);
        sortOrder.setItems(new ArrayList<>(EnumSet.allOf(Game.SortOrder.class)));
        pointType.setLabel("PointType");
        pointType.setItemLabelGenerator(Game.PointType::toString);
        pointType.setItems(new ArrayList<>(EnumSet.allOf(Game.PointType.class)));
        multiselectComboBoxTeam.setWidth("100%");
        multiselectComboBoxTeam.setLabel("Teams");
        multiselectComboBoxTeam.setPlaceholder("Choose...");
        List<Team> teamList = getTeams();
        multiselectComboBoxTeam.setItemLabelGenerator(Team::getName);
        multiselectComboBoxTeam.setItems(teamList);
        
        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    public final void edit(Game game) {
    	multiselectComboBoxTeam.clear();
    	if (game == null) {
    	    close();
    	    return;
    	   }

        final boolean persisted = game.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            this.game = gameRepository.findById(game.getId()).get();
        }
        else {
            this.game = game;
        }

        this.binder.setBean(this.game);
	this.multiselectComboBoxTeam.updateSelection(
	     	this.game.getTeams()
	     	, new HashSet<>());
		open();
		this.name.focus();
		}
		
    void save() {
        if (this.game.getName() == null || this.game.getStatus() == null || this.game.getSortOrder() == null || this.game.getPointType() == null
         || 
        this.game.getTeams() == null
        ){
            return;
        }
        			    	this.game.setTeams(multiselectComboBoxTeam.getSelectedItems());
        	gameRepository.save(this.game);
        	this.changeHandler.onChange();
    	}
	
		void delete() {
		      gameRepository.delete(this.game);
		      this.changeHandler.onChange();
		  }
	
	    public void setChangeHandler(ChangeHandler h) {
	        // ChangeHandler is notified when either save or delete is clicked
	        this.changeHandler = h;
	    }
	    
	public List<Team> getTeams() {
		return teamRepository.findAll();
	}
	
	}
