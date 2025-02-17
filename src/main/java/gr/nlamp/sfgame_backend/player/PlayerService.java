package gr.nlamp.sfgame_backend.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private static final BigInteger[] expValues;

    static {
        expValues = new BigInteger[]{
                BigInteger.valueOf(100), BigInteger.valueOf(110), BigInteger.valueOf(121),
                BigInteger.valueOf(133), BigInteger.valueOf(146), BigInteger.valueOf(161),
                BigInteger.valueOf(177), BigInteger.valueOf(250), BigInteger.valueOf(500),
                BigInteger.valueOf(750), BigInteger.valueOf(7515), BigInteger.valueOf(8925),
                BigInteger.valueOf(10335), BigInteger.valueOf(11975), BigInteger.valueOf(13715),
                BigInteger.valueOf(15730), BigInteger.valueOf(17745), BigInteger.valueOf(20250),
                BigInteger.valueOf(22755), BigInteger.valueOf(25620), BigInteger.valueOf(28660),
                BigInteger.valueOf(32060), BigInteger.valueOf(35460), BigInteger.valueOf(39535),
                BigInteger.valueOf(43610), BigInteger.valueOf(48155), BigInteger.valueOf(52935),
                BigInteger.valueOf(58260), BigInteger.valueOf(63585), BigInteger.valueOf(69760),
                BigInteger.valueOf(75935), BigInteger.valueOf(82785), BigInteger.valueOf(89905),
                BigInteger.valueOf(97695), BigInteger.valueOf(105485), BigInteger.valueOf(114465),
                BigInteger.valueOf(123445), BigInteger.valueOf(133260), BigInteger.valueOf(143425),
                BigInteger.valueOf(154545), BigInteger.valueOf(165665), BigInteger.valueOf(178210),
                BigInteger.valueOf(190755), BigInteger.valueOf(204430), BigInteger.valueOf(218540),
                BigInteger.valueOf(233785), BigInteger.valueOf(249030), BigInteger.valueOf(266140),
                BigInteger.valueOf(283250), BigInteger.valueOf(301715), BigInteger.valueOf(320685),
                BigInteger.valueOf(341170), BigInteger.valueOf(361655), BigInteger.valueOf(384360),
                BigInteger.valueOf(407065), BigInteger.valueOf(431545), BigInteger.valueOf(456650),
                BigInteger.valueOf(483530), BigInteger.valueOf(510410), BigInteger.valueOf(540065),
                BigInteger.valueOf(569720), BigInteger.valueOf(601435), BigInteger.valueOf(633910),
                BigInteger.valueOf(668670), BigInteger.valueOf(703430), BigInteger.valueOf(741410),
                BigInteger.valueOf(779390), BigInteger.valueOf(819970), BigInteger.valueOf(861400),
                BigInteger.valueOf(905425), BigInteger.valueOf(949450), BigInteger.valueOf(997485),
                BigInteger.valueOf(1045520), BigInteger.valueOf(1096550), BigInteger.valueOf(1148600),
                BigInteger.valueOf(1203920), BigInteger.valueOf(1259240), BigInteger.valueOf(1319085),
                BigInteger.valueOf(1378930), BigInteger.valueOf(1442480), BigInteger.valueOf(1507225),
                BigInteger.valueOf(1575675), BigInteger.valueOf(1644125), BigInteger.valueOf(1718090),
                BigInteger.valueOf(1792055), BigInteger.valueOf(1870205), BigInteger.valueOf(1949685),
                BigInteger.valueOf(2033720), BigInteger.valueOf(2117755), BigInteger.valueOf(2208040),
                BigInteger.valueOf(2298325), BigInteger.valueOf(2393690), BigInteger.valueOf(2490600),
                BigInteger.valueOf(2592590), BigInteger.valueOf(2694580), BigInteger.valueOf(2803985),
                new BigInteger("2158894783"), new BigInteger("2202072678"), new BigInteger("2246114132"),
                new BigInteger("2291036414"), new BigInteger("2336857143"), new BigInteger("2383594286"),
                new BigInteger("2431266171"), new BigInteger("2479891495"), new BigInteger("2529489325"),
                new BigInteger("2580079111"), new BigInteger("2631680693"), new BigInteger("2684314307"),
                new BigInteger("2738000593"), new BigInteger("2792760605"), new BigInteger("2848615817"),
                new BigInteger("2905588134"), new BigInteger("2963699896"), new BigInteger("3022973894"),
                new BigInteger("3083433372"), new BigInteger("3145102040"), new BigInteger("3208004080"),
                new BigInteger("3272164162"), new BigInteger("3337607445"), new BigInteger("3404359594"),
                new BigInteger("3472446786"), new BigInteger("3541895722"), new BigInteger("3612733636"),
                new BigInteger("3684988309"), new BigInteger("3758688075"), new BigInteger("3833861837"),
                new BigInteger("3910539073"), new BigInteger("3988749855"), new BigInteger("4068524852"),
                new BigInteger("4149895349"), new BigInteger("4232893256"), new BigInteger("4317551121"),
                new BigInteger("4403902143"), new BigInteger("4491980186"), new BigInteger("4581819790"),
                new BigInteger("4673456186"), new BigInteger("4766925310"), new BigInteger("4862263816"),
                new BigInteger("4959509092"), new BigInteger("5058699274"), new BigInteger("5159873259"),
                new BigInteger("5263070725"), new BigInteger("5368332139"), new BigInteger("5475698782"),
                new BigInteger("5585212757"), new BigInteger("5696917013"), new BigInteger("5810855353"),
                new BigInteger("5927072460"), new BigInteger("6045613909"), new BigInteger("6166526187"),
                new BigInteger("6289856711"), new BigInteger("6415653845"), new BigInteger("6543966922"),
                new BigInteger("6674846261"), new BigInteger("6808343186"), new BigInteger("6944510049"),
                new BigInteger("7083400250"), new BigInteger("7225068255"), new BigInteger("7369569621"),
                new BigInteger("7516961013"), new BigInteger("7667300233"), new BigInteger("7820646238"),
                new BigInteger("7977059163"), new BigInteger("8136600346"), new BigInteger("8299332353"),
                new BigInteger("8465319000"), new BigInteger("8634625380"), new BigInteger("8807317888"),
                new BigInteger("8983464245"), new BigInteger("9163133530"), new BigInteger("9346396201"),
                new BigInteger("9533324125"), new BigInteger("9723990607"), new BigInteger("9918470419"),
                new BigInteger("10116839828"), new BigInteger("10319176624"), new BigInteger("10525560157"),
                new BigInteger("10736071360"), new BigInteger("10950792787"), new BigInteger("11169808643"),
                new BigInteger("11393204816"), new BigInteger("11621068912"), new BigInteger("11853490290"),
                new BigInteger("12090560096"), new BigInteger("12332371298"), new BigInteger("12579018724"),
                new BigInteger("12830599099"), new BigInteger("13087211081"), new BigInteger("13348955302"),
                new BigInteger("13615934408"), new BigInteger("13888253096"), new BigInteger("14166018158"),
                new BigInteger("14449338521"), new BigInteger("14738325292"), new BigInteger("15033091798"),
                new BigInteger("15333753634"), new BigInteger("15640428706"), new BigInteger("15953237280")
        };
    }

    public void addExperience(final Player player, final BigInteger experience) {
        player.setGainedExperience(player.getGainedExperience().add(experience));

        BigInteger expForNextLevel = getExperienceForNextLevel(player.getLevel());
        while (player.getGainedExperience().compareTo(expForNextLevel) >= 0) {
            player.setLevel(player.getLevel() + 1);
            expForNextLevel = getExperienceForNextLevel(player.getLevel());
        }
    }

    private static BigInteger getExperienceForNextLevel(final long level) {
        final int levelArrayIndex = (int) (level - 1);
        BigInteger experience;

        if(levelArrayIndex > 768){
            experience = expValues[768];

            final BigDecimal multiplier = new BigDecimal("1.4");
            for(int i = 768; i < levelArrayIndex; i++) {
                BigDecimal experienceDecimal = new BigDecimal(experience);

                experienceDecimal = experienceDecimal.multiply(multiplier);

                experience = experienceDecimal.toBigInteger();
            }

            final BigInteger max = new BigInteger("4.3026616884253E+303");
            if(experience.compareTo(max) > 0)
                experience = new BigInteger("4.3026616884253E+303");

            return experience;
        }
        else
            return expValues[levelArrayIndex];
    }

}
