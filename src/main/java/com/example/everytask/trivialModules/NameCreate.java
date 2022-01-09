package com.example.everytask.trivialModules;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Getter
@Setter
@Component
public class NameCreate {
    ArrayList<String> firstNames = new ArrayList<>(Arrays.asList(
            "화난",
            "귀여운",
            "건강한",
            "똑똑한",
            "무심한",
            "미련한",
            "깐죽대는",
            "재빠른",
            "여린",
            "순수한",
            "용감한",
            "겁없는",
            "다정한",
            "행복한",
            "슬픈"
    ));
    ArrayList<String> secondNames = new ArrayList<String>(Arrays.asList(
            "사신",
            "해적",
            "바야바",
            "엘프",
            "악마",
            "검투사",
            "제우스",
            "현자",
            "드라큘라",
            "천사",
            "바이킹",
            "지니",
            "황제",
            "기사",
            "갓파쿠"
    ));

    public String randomName(){
        String firstName = firstNames.get(new Random().nextInt(firstNames.size()));
        String secondName = secondNames.get(new Random().nextInt(secondNames.size()));
        return firstName + " " + secondName;
    }

}
