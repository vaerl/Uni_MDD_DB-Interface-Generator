	package de.thm.dbiGenerator.grids;
	
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
	import de.thm.dbiGenerator.entities.Team;
	import de.thm.dbiGenerator.repos.TeamRepository;
	import de.thm.dbiGenerator.details.TeamDetails;
	import de.thm.dbiGenerator.editors.TeamEditor;
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
	public class TeamGridPage extends VerticalLayout {
	
	    private static final long serialVersionUID = -8733687422451328748L;
	    private static final Logger log = LoggerFactory.getLogger(TeamGridPage.class);
	
	    private TeamRepository TeamRepository;
	    private Grid<Team> grid;
	    private TeamEditor TeamEditor;
	
	    private TextField filter;
	
	    private Button evaluate;
	
	    @Autowired
	    public TeamGridPage(TeamRepository TeamRepository, TeamEditor TeamEditor) {
	        super();
	        this.TeamRepository = TeamRepository;
	        this.TeamEditor = TeamEditor;
	
	        filter = new TextField();
	        HorizontalLayout actions = new HorizontalLayout();
	
	        // grid
	        grid = new Grid<>(Team.class);
	        grid.setItems(TeamRepository.findAll());
	        grid.setMultiSort(true);
	        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
	                GridVariant.LUMO_ROW_STRIPES);
	        grid.asSingleSelect().addValueChangeListener(e -> this.TeamEditor.edit(e.getValue()));
	        // add Columns
	        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
	        grid.asSingleSelect().addValueChangeListener(e -> this.TeamEditor.edit(e.getValue()));
	        //add Columns
	        setColumns();
	
	        // actions
	        Button addNew = new Button("Team hinzufügen", VaadinIcon.PLUS.create());
	        addNew.addClickListener(e -> this.TeamEditor.edit(new Team()));
	
	        // filter
	        filter.setPlaceholder("Nach Namen filtern");
	        filter.setValueChangeMode(ValueChangeMode.EAGER);
	        filter.addValueChangeListener(e -> listValues(e.getValue()));
	
	        // editor
	        TeamEditor.setChangeHandler(() -> {
	            TeamEditor.close();
	            listValues(filter.getValue());
	        });
	
	
	        actions.add(filter, addNew);
	        add(actions, grid, this.TeamEditor);
	        listValues(null);
	    }
	
	    void listValues(String filterText) {
	        if (StringUtils.isEmpty(filterText)) {
	            grid.setItems(TeamRepository.findAll());
	        } else {
	            grid.setItems(TeamRepository.findByNameStartsWithIgnoreCase(filterText));
	        }
	    }
	
	    private void setColumns() {
	        // remove unwanted columns
	        grid.removeAllColumns();
	        // add Columns
	        grid.addColumn(Team::getStatus.setHeader("Status".setSortable(true)
	        grid.addColumn(Team::getName.setHeader("Name".setSortable(true)
	        grid.addColumn(Team::getPoints.setHeader("Points".setSortable(true)
	        grid.addColumn(Team::getGender.setHeader("Gender".setSortable(true)
	
	
	        // add standard-columns

	        grid.addComponentColumn(value -> {
	            Button details = new Button("Fertig");
	            details.addClassName("details");
	            details.addClickListener(e -> {
	                var TeamDetails = new TeamDetails();
	                TeamDetails.open(value);
	            });
	            if (value.getFinished().isEmpty()) {
	                log.info("Finished is empty.");
	                details.setEnabled(false);
	            } else {
	                log.info("Finished will be displayed.");
	                details.setEnabled(true);
	            }
	            return details;
	        });
	
	        grid.addComponentColumn(value -> {
	            Button edit = new Button("Bearbeiten");
	            edit.addClassName("edit");
	            edit.addClickListener(e -> {
	                TeamEditor.edit(value);
	            });
	            return edit;
	        });
	
	    }
	}
	
