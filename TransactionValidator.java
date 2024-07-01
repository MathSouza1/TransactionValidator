import java.util.List;

public class TransactionValidator {

    private static final Logger logger = LoggerFactory.getLogger(TransactionValidator.class);
    private static final List<String> list = List.of("02", "03", "04", "05", "12");

    public void validate(ISOModel model) {
        logger.info("Starting Validation.");

        boolean bit02NotFilled = model.getBit02() == null;
        boolean bit02FilledAndValueEmpty = !bit02NotFilled && model.getBit02().getValue().isEmpty();
        boolean bit02EmptyAndBit03Null = bit02FilledAndValueEmpty && model.getBit03() == null;
        String valueNotFilled = bit02NotFilled ? "01" : "02";

        try {
            if (validateEmptyBits(bit02NotFilled, bit02FilledAndValueEmpty, bit02EmptyAndBit03Null, valueNotFilled)){
                if (validateNullBits(model.getBit03(), model.getBit04(), model.getBit05(), model.getBit12())) {
                    save(model);
                }
            } else {
                throw new IllegalArgumentException("Values NOT filled in correctly");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private boolean validateEmptyBits(boolean bit02NotFilled, boolean bit02FilledAndValueEmpty, boolean bit02EmptyAndBit03Null, String valueNotFilled) {
        return !(bit02NotFilled || bit02FilledAndValueEmpty || bit02EmptyAndBit03Null || valueNotFilled.equals("01"));
    }

    private boolean validateNullBits(ISOModel bit03, ISOModel bit04, ISOModel bit05, ISOModel bit12) {
        return bit03 != null && (bit04 != null && list.contains("10")) && bit05 != null && bit12 != null;
    }

    private void save(ISOModel model) {
        logger.info("Saving transaction " + model.getBit02().getValue());
    }
}
