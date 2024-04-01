package com.impacus.maketplace.common.utils;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.service.CouponAdminService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class CouponUtils {
    private static List<Integer> percentages = Arrays.asList(10,20,30,40,50);
    public CouponUtils() throws Exception {
        throw new Exception("you do not need to construct CouponUtils class!");
    }

    public static String getUUIDCouponCode() {
        return UUID.randomUUID().toString();
    }

    public static LocalDateTime getRandomExpiredAt(LocalDateTime fromDate) {
        // add random expired days from now date (1 day ~ 7 days)
        return fromDate.plusDays((long)(Math.random() * 7) + 1);
    }

    public static Integer getRandomDiscountRate() {
        return percentages.get((int) (Math.random() * (percentages.size())));
    }

    public static void validateCouponCode(String code) throws CustomException {
        if(!Pattern.matches(CouponAdminService.COUPON_CODE, code)) {
            throw new CustomException(ErrorType.INVALID_COUPON_FORMAT, "Invalid format of coupon code.");
        }
    }

    public static <E extends Enum<E> & CommonFieldInterface> E fromCode(Class<E> enumClass, String code) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst().orElseThrow(() -> new CustomException(ErrorType.UNKNOWN));
    }

    public interface CommonFieldInterface {
        String getCode();
        String getValue();
    }

    public static String generateCode() {
        final int CHAR_LENGTH = 8;

        final char[] charTable = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',};

        Random random = new Random(System.currentTimeMillis());
        int charSize = charTable.length;
        StringBuffer buffer = new StringBuffer();

        for (int i =0; i < charSize; i++) {
            buffer.append(charTable[random.nextInt(charSize)]);
        }
        return buffer.toString();

    }
}
