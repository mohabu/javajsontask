package com.barclays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class CountriesProcessingData {
    private Map<String, Double> unSortedMapCountriesOrder = new HashMap<>();
    private JSONParser parser = new JSONParser();



    public void showData(){
        dataProcessing();
    }

    private void dataProcessing(){

        try {
            Object workData = getParser().parse(new FileReader("Conutries.json"));
            JSONObject jsonObject = (JSONObject)workData;
            JSONArray contriesObjectsRatesArray = (JSONArray) jsonObject.get("rates");
            Iterator<Object> iterator = contriesObjectsRatesArray.iterator();

            while (iterator.hasNext()){
                JSONObject countryObject = (JSONObject) iterator.next();
                JSONArray countryPeriods = (JSONArray) countryObject.get("periods");
                String countryName= (String) countryObject.get("name");

               /*work just with first object as of today within the EU*/
                Iterator<Object> countryPeriodsRatesIterator = countryPeriods.subList(0,1).iterator();

                while (countryPeriodsRatesIterator.hasNext() ){
                    JSONObject countryperiodsFirstObject = (JSONObject)countryPeriodsRatesIterator.next();
                    Object countryperiodsFirstObjectRates = countryperiodsFirstObject.get("rates");
                    JSONObject ratesStandardVatObject = (JSONObject)countryperiodsFirstObjectRates;
                    double countrySatandardRateNumber = (double) ratesStandardVatObject.get("standard");
                    getUnSortedCountriesMapRates().put(countryName, countrySatandardRateNumber);
                }
            }

            System.out.println("****************************************************************");

            sortCountriesByValueStandaredVat();
            System.out.println("****************************************************************");

        }
        catch (FileNotFoundException e){e.printStackTrace();}

        catch (IOException e){e.printStackTrace();}

        catch (ParseException e){e.printStackTrace();}

        catch (Exception e){e.printStackTrace();}
    }


    private void sortCountriesByValueStandaredVat() {
        Map<String, Double> unSortedCountriesVat = getUnSortedCountriesMapRates();
        System.out.println("Unsorted Countries Order: " + unSortedCountriesVat);
        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
        unSortedCountriesVat.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        System.out.println("Sorted Countries Order: " + sortedMap);

        LinkedHashMap<String, Double> reverseSortedMap = new LinkedHashMap<>();
        sortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        System.out.println("Reverse Sorted Countries Order: " + reverseSortedMap);

        ascendingCountriesOrderVat(sortedMap, "EU countries with the lowest standard VAT rate");
        System.out.println("****************************************************************");
        descendingCountriesOrderVat(reverseSortedMap, "EU countries with the highest standard VAT rate");
    }



    private void descendingCountriesOrderVat(LinkedHashMap<String, Double> reverseSortedMap, String s) {
        HashMap<Double, ArrayList<String>> elementsFromDscendingresult = new LinkedHashMap<>();

        for (String key : reverseSortedMap.keySet()) {
            ArrayList<String> colName = null;
            if (!elementsFromDscendingresult.containsKey(reverseSortedMap.get(key))) {
                colName = new ArrayList<String>();
                colName.add(key);
                elementsFromDscendingresult.put(reverseSortedMap.get(key), colName);
            } else {
                colName = elementsFromDscendingresult.get(reverseSortedMap.get(key));
                colName.add(key);
                elementsFromDscendingresult.put(reverseSortedMap.get(key), colName);
            }
        }

        System.out.println(s);

        int j = 0;
        for (Double key : elementsFromDscendingresult.keySet()) {

            System.out.println(key + "\t" + elementsFromDscendingresult.get(key));
            j++;
            if (j == 3) {
                break;
            }
        }
    }

    private void ascendingCountriesOrderVat(LinkedHashMap<String, Double> sortedMap, String s) {
        HashMap<Double, ArrayList<String>> elementsFromAscendingresult = new LinkedHashMap<>();

        for (String key : sortedMap.keySet()) {
            ArrayList<String> colName = null;
            if (!elementsFromAscendingresult.containsKey(sortedMap.get(key))) {
                colName = new ArrayList<String>();
                colName.add(key);
                elementsFromAscendingresult.put(sortedMap.get(key), colName);
            } else {
                colName = elementsFromAscendingresult.get(sortedMap.get(key));
                colName.add(key);
                elementsFromAscendingresult.put(sortedMap.get(key), colName);
            }
        }
        System.out.println("****************************************************************");
        System.out.println(s);
        int i = 0;
        for (Double key : elementsFromAscendingresult.keySet()) {
            System.out.println(key + "\t" + elementsFromAscendingresult.get(key));
            i++;
            if (i == 3) {
                break;
            }
        }
    }


    private  Map<String, Double> getUnSortedCountriesMapRates() {
        return unSortedMapCountriesOrder;
    }

    public JSONParser getParser() {
        return parser;
    }


}
