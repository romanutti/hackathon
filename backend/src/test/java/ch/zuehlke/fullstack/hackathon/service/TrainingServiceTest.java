package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.model.Training;
import ch.zuehlke.fullstack.hackathon.model.TrainingResponse;
import ch.zuehlke.fullstack.hackathon.service.http.InsightClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainingServiceTest {
    private static final String VALID_ID = "feva";
    private static final long CURRENT_YEAR = LocalDate.now().getYear();
    private TrainingService testee;
    private InsightClient insightClient;

    @BeforeEach
    void setUp() {
        this.insightClient = mock(InsightClient.class);
        this.testee = new TrainingService(insightClient);
    }
    
    @Test
    void getTrainingFromCurrentAndLastYear_filtersOutOlderTrainings() {
        Training trainingThisYear = new Training("Recent training", 0, List.of(), 0);
        Training trainingLastYear = new Training("Semi recent training", 1, List.of(), 1);
        Training trainingThreeYearsAgo = new Training("Older training", 2, List.of(), 2);
        List<TrainingResponse> responses = List.of(
                new TrainingResponse(CURRENT_YEAR, trainingThisYear),
                new TrainingResponse(CURRENT_YEAR - 1, trainingLastYear),
                new TrainingResponse(CURRENT_YEAR - 2, trainingThreeYearsAgo));
        when(insightClient.getTrainings(VALID_ID)).thenReturn(responses);
        
        List<Training> trainings = testee.getTrainingsFromCurrentAndLastYear(VALID_ID);
        
        assertEquals(2, trainings.size());
        assertTrue(trainings.containsAll(List.of(trainingThisYear, trainingLastYear)));
    }
}