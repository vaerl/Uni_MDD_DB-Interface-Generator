package de.thm.dbiGenerator.pages;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import de.thm.dbiGenerator.entities.Game;
import de.thm.dbiGenerator.repositories.GameRepository;
//import de.thm.dbiGenerator.details.GameDetails;
import de.thm.dbiGenerator.editors.GameEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
// Team Repository entfernt, da nur für Play verwendet

@Component
@Transactional
@UIScope
public class GameGridPage extends VerticalLayout {

    private static final long serialVersionUID = -8733687422451328748L;
    private static final Logger log = LoggerFactory.getLogger(GameGridPage.class);

    private GameRepository GameRepository;
    private Grid<Game> grid;
    private GameEditor GameEditor;

    private TextField filter;

    private Button evaluate;

    @Autowired
    public GameGridPage(GameRepository GameRepository, GameEditor GameEditor) {
        super();
        this.GameRepository = GameRepository;
        this.GameEditor = GameEditor;

        filter = new TextField();
        HorizontalLayout actions = new HorizontalLayout();

        // grid
        grid = new Grid<>(Game.class);
        grid.setItems(GameRepository.findAll());
        grid.setMultiSort(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(e -> this.GameEditor.edit(e.getValue()));
        // add Columns
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(e -> this.GameEditor.edit(e.getValue()));
        //add Columns
        setColumns();

        // actions
        Button addNew = new Button("Game hinzufügen", VaadinIcon.PLUS.create());
        addNew.addClickListener(e -> this.GameEditor.edit(new Game()));

        // filter
        filter.setPlaceholder("Nach Namen filtern");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listValues(e.getValue()));

        // editor
        GameEditor.setChangeHandler(() -> {
            GameEditor.close();
            listValues(filter.getValue());
        });


        actions.add(filter, addNew);
        add(actions, grid, this.GameEditor);
        listValues(null);
    }

    void listValues(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(GameRepository.findAll());
        } else {
            grid.setItems(GameRepository.findByNameStartsWithIgnoreCase(filterText));
        }
    }

    private void setColumns() {
        // remove unwanted columns
        grid.removeAllColumns();
        // add Columns
        grid.addColumn(Game::getName).setHeader("Name").setSortable(true);
        grid.addColumn(Game::getStatus).setHeader("Status").setSortable(true);
        grid.addColumn(Game::getSortOrder).setHeader("SortOrder").setSortable(true);
        grid.addColumn(Game::getPointType).setHeader("PointType").setSortable(true);


        // add standard-columns

//        grid.addComponentColumn(value -> {
//            Button details = new Button("Fertig");
//            details.addClassName("details");
//            details.addClickListener(e -> {
//                var GameDetails = new GameDetails();
//                GameDetails.open(value);
//            });
//            if (value.getFinished().isEmpty()) {
//                log.info("Finished is empty.");
//                details.setEnabled(false);
//            } else {
//                log.info("Finished will be displayed.");
//                details.setEnabled(true);
//            }
//            return details;
//        });

        grid.addComponentColumn(value -> {
            Button edit = new Button("Bearbeiten");
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                GameEditor.edit(value);
            });
            return edit;
        });

    }
}
	
