package lt.kg.vilnius;

import lt.kg.vilnius.garden.BuildingStateEntity;
import lt.kg.vilnius.garden.GardenEntity;
import lt.kg.vilnius.garden.MissedDaysEntity;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Pavel on 2017-05-27.
 */
public class CsvUtils {
    /**
     * Parses csv with missed days info to {@Link} missed days entities
     *
     * @return
     * @throws IOException
     */
    public static List<MissedDaysEntity> parseMissedDaysInfo() throws IOException {
        Reader in = new FileReader("./src/main/resources/csv/missed-days.csv");
        Iterator<CSVRecord> records = CSVFormat.DEFAULT.parse(in).iterator();
        List<MissedDaysEntity> missedDaysEntities = new ArrayList<>();
        records.next();
        records.forEachRemaining(record -> {
            MissedDaysEntity missedDaysEntity = new MissedDaysEntity();
            missedDaysEntity.setLabel(record.get(0));
            missedDaysEntity.setSickMissedDays(Long.valueOf(record.get(2)));
            missedDaysEntity.setOtherApprovedMissedDays(Long.valueOf(record.get(3)));
            missedDaysEntity.setNorApprovedMissedDays(Long.valueOf(record.get(4)));
            missedDaysEntity.setAmountOfKids(Long.valueOf(record.get(1)));
            missedDaysEntities.add(missedDaysEntity);
        });
        return missedDaysEntities;

    }

    /**
     * Parses building state info from csv to entities
     * @return List of {@link BuildingStateEntity}
     * @throws IOException
     */
    public static List<BuildingStateEntity> parseBuildingState() throws IOException {
        List<BuildingStateEntity> buildingStates = new ArrayList<>();
        Reader in = new FileReader("./src/main/resources/csv/building-state.csv");
        Iterator<CSVRecord> records = CSVFormat.DEFAULT.parse(in).iterator();
        List<MissedDaysEntity> missedDaysEntities = new ArrayList<>();
        records.next();
        records.forEachRemaining(record -> {
            BuildingStateEntity entity = new BuildingStateEntity();
            entity.setLabel(record.get(0));
            entity.setInsideState((Float) getNumber(record.get(11)));
            entity.setOutsideState((Float) getNumber(record.get(10)));
            if (entity.getInsideState() != null && entity.getOutsideState() != null){
                buildingStates.add(entity);
            }
        });
        return buildingStates;
    }

    private static Number getNumber(String s) {
        try {
            return Float.valueOf(s);
        } catch (NumberFormatException e){

            return null;
        }
    }

    public enum HeadersGeneral {
        ID, LABEL, ADDRESS, PHONE, SCHOOLNO, WWW, EMAIL, SCHOOL_TYPE, BUILDDATE, ELDERATE
    }

    /**
     * Parses csv with kindergartens info to {@Link GardenEntity}
     *
     * @return List of {@Link GardenEntity}
     * @throws IOException
     */
    public static List<GardenEntity> parseGeneralGardensInfo() throws IOException {
        Reader in = new FileReader("./src/main/resources/csv/gardens.csv");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader(HeadersGeneral.class).parse(in);
        List<GardenEntity> gardenEntities = new ArrayList<>();
        records.iterator().next();
        for (CSVRecord record : records) {
            GardenEntity garden = new GardenEntity();
            garden.setLabel(record.get(HeadersGeneral.LABEL));
            garden.setAddress(record.get(HeadersGeneral.ADDRESS));
            garden.setPhone(record.get(HeadersGeneral.PHONE));
            garden.setSchoolNo(Long.valueOf(record.get(HeadersGeneral.SCHOOLNO)));
            garden.setWww(record.get(HeadersGeneral.WWW));
            garden.setEmail(record.get(HeadersGeneral.EMAIL));
            garden.setSchoolType(record.get(8));
            garden.setBuildDate(getDate(record));
            garden.setElderate(record.get(10));
            gardenEntities.add(garden);
        }
        return gardenEntities;
    }

    /**
     * Return date in csv or if failed 1111-11-11
     *
     * @param record
     * @return {@link Date} object
     */
    private static Date getDate(CSVRecord record) {
        DateFormat format = new SimpleDateFormat("YYYY-MM-DD");
        Date date = null;
        try {
            date = format.parse(record.get(9));
        } catch (ParseException e) {
            try {
                date = format.parse("1111-11-11");
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return date;
    }
}
