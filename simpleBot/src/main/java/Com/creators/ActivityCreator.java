package Com.creators;

import Com.spellcheckers.SpellCheckerExample;
import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ActivityTypes;
import org.springframework.stereotype.Component;

@Component
public class ActivityCreator {

    private static final String spellCheckedResponsePart = "You have probably mean: ";
    private static final String echoResponsePart = "You typed: ";
    private static SpellCheckerExample spellChecker = new SpellCheckerExample();

    private ActivityCreator() {

    }

    public static Activity createSpellCheckedActivity(Activity activity) {
        return createEmptyActivity(activity)
                .withText(spellCheckedResponsePart + spellChecker.getCorrectedText(activity.text()));
    }

    public static Activity createEchoActivity(Activity activity) {
        return createEmptyActivity(activity)
                .withText(echoResponsePart + activity.text());
    }

    private static Activity createEmptyActivity(Activity activity) {
        return new Activity()
                .withType(ActivityTypes.MESSAGE)
                .withRecipient(activity.from())
                .withFrom(activity.recipient());
    }
}