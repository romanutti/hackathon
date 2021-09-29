package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.model.Training;
import ch.zuehlke.fullstack.hackathon.model.TrainingResponse;
import ch.zuehlke.fullstack.hackathon.service.http.InsightClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {

    private final InsightClient insightClient;

    public TrainingService(InsightClient insightClient) {
        this.insightClient = insightClient;
    }

    public List<Training> getTrainingsFromLastTwoYears(String userId) {
        List<TrainingResponse> trainingResponseList = insightClient.getTrainings(userId);
        return trainingResponseList.stream()
                .filter(trainingResponse -> trainingHasHappenedInTheLastTwoYears(trainingResponse.getYear()))
                .map(trainingResponse -> trainingResponse.getTraining())
                .collect(Collectors.toList());
    }

    private boolean trainingHasHappenedInTheLastTwoYears(long year) {
        return year >= LocalDate.now().minusYears(year - 1).getYear();
    }
}