package io.github.mivek.utils;

import io.github.mivek.internationalization.Messages;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to convert data.
 *
 * @author mivek
 */
public final class Converter {

    /**
     * Arrays of cardinal directions.
     */
    private static final String[] DIRECTIONS = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
            "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};

    /**
     * Statue miles to kilometers ratio.
     **/
    private static final Double SM_TO_KM = 1.609344;

    /**
     * Private constructor.
     */
    private Converter() {
    }

    /**
     * This method converter degrees to direction.
     *
     * @param degreesStr a string representing the degrees.
     * @return A string for the direction.
     */
    public static String degreesToDirection(final String degreesStr) {
        double degrees;
        try {
            degrees = Double.parseDouble(degreesStr);
        } catch (NumberFormatException e) {
            return Messages.getInstance().getString("Converter.VRB");
        }

        return Messages.getInstance().getString("Converter." + DIRECTIONS[(int) ((degrees + 11.5) / 22.5) % DIRECTIONS.length]);
    }

    /**
     * Converts a string representing the visibility into a real visibility.
     *
     * @param input A string.
     * @return a string correctly formatted.
     */
    public static String convertVisibility(final String input) {
        if ("9999".equals(input)) {
            return ">10km";
        } else {
            return Integer.parseInt(input) + "m";
        }
    }

    /**
     * Converts the metar part of temperature into integer.
     *
     * @param input The metar part of the temperature.
     * @return an integer of the temperature.
     */
    public static int convertTemperature(final String input) {
        if (input.startsWith("M")) {
            return -(Integer.parseInt(input.substring(1, 3)));
        } else {
            return Integer.parseInt(input);
        }
    }

    /**
     * Converts inches of mercury pressure into hecto pascals.
     *
     * @param inchesMercury string of mercury.
     * @return double of the pressure in Pascals.
     */
    public static double inchesMercuryToHPascal(final double inchesMercury) {
        return 33.8639 * inchesMercury;
    }

    /**
     * Converts a string representing a time to a LocalTime.
     * Eg: "0830" returns a LocalTime of 08:30.
     *
     * @param input the string to convert.
     * @return the corresponding time.
     */
    public static LocalTime stringToTime(final String input) {
        int hours = Integer.parseInt(input.substring(0, 2));
        int minutes = Integer.parseInt(input.substring(2));
        return LocalTime.of(hours, minutes);
    }

    /**
     * Converts a string representing a precipitation amount in inches.
     * @param input The string to convert
     * @return The actual amount.
     */
    public static float convertPrecipitationAmount(final String input) {
        return Float.parseFloat(input) / 100;
    }

    /**
     * Converts the temperature for the remark.
     * @param sign The sign of the temperature: 1 means below 0, 0 means positive
     * @param temperature The string representing the temperature
     * @return The temperature as a float
     */
    public static float convertTemperature(final String sign, final String temperature) {
        float temp = Float.parseFloat(temperature) / 10;
        return "0".equals(sign) ? temp : -1 * temp;
    }

    /**
     * Converts the visibility to a value in km.
     *
     * @param visibility The main visibility of a Visibility object
     * @return The visibility in km as a double
     */
    public static Double convertVisibilityToKM(final String visibility) {
        final Matcher matcher = Pattern.compile("(\\d+)([a-z,A-Z]+)").matcher(visibility.replace(">", ""));
        if (!matcher.find()) {
            return null;
        }

        final int value = Integer.parseInt(matcher.group(1));
        final String unit = matcher.group(2).toUpperCase();

        final Double result;
        switch (unit) {
            case "SM" ->  result = value * SM_TO_KM;
            case "KM" -> result = (double) value;
            case "M" -> result = value / 1000.0;
            default -> result = null;
        }

        return result;
    }

    /**
     * Converts kilometers to statue miles.
     * @param km kilometers
     * @return value in statue miles
     */
    public static Double kmToSM(final Double km) {
        return km / SM_TO_KM;
    }
}
