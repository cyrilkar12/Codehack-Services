package com.codehack;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.api.java.function.Function;
import java.util.Arrays;
import java.util.List;

public class Test_ItemFrequency {

    public static void main(String args[]) {

      /*  SparkConf conf = new SparkConf().setAppName("FP-Growth_ItemFrequency").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> data = sc.textFile("C:\\Cyril\\Eclipse-ws\\Codehack-Services\\src\\main\\resources\\mlib\\sample_fpgrowth.txt");

        JavaRDD<List<String>> transactions = data.map(new Function<String, List<String>>() {
            public List<String> call(String line) {
                String[] parts = line.split(" ");
                return Arrays.asList(parts);
            }
        });

        FPGrowth fpg = new FPGrowth().setMinSupport(0.2).setNumPartitions(1);
        FPGrowthModel<String> model = fpg.run(transactions);

        model.freqItemsets().saveAsTextFile("/home/data/itemset");

        sc.stop();*/
    	SparkConf conf = new SparkConf().setAppName("FP-Growth_ItemFrequency").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> data = sc.textFile("C:\\Cyril\\Eclipse-ws\\Codehack-Services\\src\\main\\resources\\mlib\\sample_fpgrowth1.txt");

    	JavaRDD<List<String>> transactions = data.map(line -> Arrays.asList(line.split(" ")));

    	FPGrowth fpg = new FPGrowth()
    	  .setMinSupport(0.3)
    	  .setNumPartitions(5);
    	FPGrowthModel<String> model = fpg.run(transactions);

    	for (FPGrowth.FreqItemset<String> itemset: model.freqItemsets().toJavaRDD().collect()) {
    	  System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>[" + itemset.javaItems() + "], " + itemset.freq());
    	}

    	double minConfidence = 0.9;
    	for (AssociationRules.Rule<String> rule
    	  : model.generateAssociationRules(minConfidence).toJavaRDD().collect()) {
    	  System.out.println(
    	    rule.javaAntecedent() + " => " + rule.javaConsequent() + ", " + rule.confidence());
    	}
    }
}