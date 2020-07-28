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
import de.thm.dbiGenerator.entities.Game;
import de.thm.dbiGenerator.repos.GameRepository;
import de.thm.dbiGenerator.ChangeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumSet;

@Component
@UIScope
public class GameEditor extends Dialog implements KeyNotifier {

    private GameRepository GameRepository;
    private ChangeHandler changeHandler;
    private Game game;

    //buttons
    Button save = new Button("Speichern", VaadinIcon.CHECK.create());
    Button cancel = new Button("Abbrechen");
    Button delete = new Button("Löschen", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    //fields to edit
    TextField name = new TextField("Game-Name");
	Select<Game.Status> status = new Select<>();
	Select<Game.SortOrder> sortOrder = new Select<>();
	Select<Game.PointType> pointType = new Select<>();
	HorizontalLayout fields = new HorizontalLayout(name, status, sortOrder, pointType);
	Binder<Game> binder = new Binder<>(Game.class);

    @Autowired
    public GameEditor(GameRepository GameRepository) {
	   	super();
   	    this.GameRepository = GameRepository;
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
        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    public final void edit(Game Game) {
        if (Game == null) {
            close();
            return;
        }

        final boolean persisted = Game.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            this.Game = GameRepository.findById(Game.getId()).get();
        }
        else {
            this.Game = Game;
        }

        this.binder.setBean(this.Game);
        open();
        this.name.focus();
    }

    void save() {
        if (this.Game.getStatus() == null || this.Game.getSortOrder() == null || this.Game.getPointType() == null || this.Game.getName() == null){
            return;
        }
        GameRepository.save(this.Game);
        this.changeHandler.onChange();
    }

	void delete() {
       GameRepository.delete(this.Game);
       this.changeHandler.onChange();
   }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete is clicked
        this.changeHandler = h;
    }

}
