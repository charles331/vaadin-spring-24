package org.vaadin.example.view;


import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.config.Chart;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.broadcaster.Broadcaster;
import org.vaadin.example.entity.Vote;
import org.vaadin.example.service.VoteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "results")
@PageTitle("Results | Vote App")
public class ResultsView extends VerticalLayout {

    private final VoteService voteService;
    private final Grid<Vote> grid = new Grid<>(Vote.class);
    private final ApexCharts barChart= new ApexCharts();

    @Autowired
    public ResultsView(VoteService voteService) {
        this.voteService = voteService;
        setSizeFull(); // Ensure the layout takes the full size
        configureGrid();
        add(grid);
        updateList();
        Broadcaster.register(message -> {
            getUI().ifPresent(ui -> ui.access(() -> {
                // Mettre à jour votre vue avec les nouvelles données ici
                System.out.println("Broadcaster actionned !");
                updateList();
            }));
        });

        configureChart();
        add(barChart);
    }

    private void configureChart() {
        Chart chart= new Chart();
        chart.setType(Type.BAR);
        barChart.setChart(chart);
        barChart.setSeries(new Series("Votes", 55, 60, 75, 80));
        barChart.setLabels("Option 1", "Option 2", "Option 3", "Option 4");
        barChart.setWidth("500px");
        barChart.setHeight("250px");
    }


    private void configureGrid() {
        //grid.setSizeFull();
        grid.setWidth("500px");
        grid.setHeight("250px");
        grid.removeAllColumns(); // Remove auto-generated columns

        // Explicitly add columns
        grid.addColumn(Vote::getVoterName).setHeader("Voter Name");
        grid.addColumn(Vote::getRating).setHeader("Rating");
        grid.addColumn(Vote::getComment).setHeader("Comment");
        grid.addColumn(Vote::getSuggestion).setHeader("Suggestion");

        // Alternatively, if you want to use property names directly, uncomment the following line
        // grid.setColumns("voterName", "rating", "comment", "suggestion");
    }

    private void updateList() {
        //grid.setItems(voteService.findAllVotes()); // Fetch and display all votes
        List<Vote> votes = voteService.findAllVotes(); // Fetch all votes
        grid.setItems(votes); // Update the grid with the new list of votes


        // Assuming you're grouping by some category of vote, let's say 'rating',
        // and you want to count votes for each rating. This example assumes 'rating' is an int.
        Map<Integer, Long> votesByRating = votes.stream()
                .collect(Collectors.groupingBy(Vote::getRating, Collectors.counting()));

        // Transform the map into series data
        List<Number> seriesData = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        votesByRating.forEach((rating, count) -> {
            labels.add("Rating " + rating);
            seriesData.add(count);
        });

        // Update the chart
        updateChart(seriesData, labels);
    }

    private void updateChart(List<Number> seriesData, List<String> labels) {
        // Assuming there's only one series you want to update
        Series series = new Series();
        series.setName("Votes");
        series.setData(seriesData.toArray(new Number[0]));

        // Set labels
        barChart.setLabels(labels.toArray(new String[0]));

        // Update the series
        barChart.updateSeries(series);
    }

}
