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

    public List<Training> getTrainingsFromCurrentAndLastYear(String userId) {
        List<TrainingResponse> trainingResponseList = insightClient.getTrainings(userId);
        return trainingResponseList.stream()
                .filter(trainingResponse -> trainingHasHappenedInCurrentOrLastYear(trainingResponse.getYear()))
                .map(TrainingResponse::getTraining)
                .collect(Collectors.toList());
    }

    private boolean trainingHasHappenedInCurrentOrLastYear(long year) {
        return year > LocalDate.now().minusYears(2).getYear();
    }
}