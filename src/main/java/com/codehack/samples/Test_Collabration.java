package com.codehack.samples;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.spark.SparkConf;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.rdd.RDD;

import com.codehack.UserRecommendations;

import scala.Tuple2;

public class Test_Collabration {
public static void main(String[] args) {
	//SparkConf conf = new SparkConf().setAppName("Java Collaborative Filtering Example");
	//JavaSparkContext jsc = new JavaSparkContext(conf);
 	SparkConf conf = new SparkConf().setAppName("Collaborative_Filtering").setMaster("local");
    JavaSparkContext jsc = new JavaSparkContext(conf);
	// Load and parse the data
	String path = "C:\\\\Cyril\\\\Eclipse-ws\\\\Codehack-Services\\\\src\\\\main\\\\resources\\\\mlib\\\\test_collabrative.data";
	JavaRDD<String> data = jsc.textFile(path);
	JavaRDD<Rating> ratings = data.map(s -> {
	  String[] sarray = s.split(",");
	  return new Rating(Integer.parseInt(sarray[0]),
	    Integer.parseInt(sarray[1]),
	    Double.parseDouble(sarray[2]));
	});

	// Build the recommendation model using ALS
	int rank = 10;
	int numIterations = 10;
	MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(ratings), rank, numIterations, 0.01);

	// Evaluate the model on rating data
	JavaRDD<Tuple2<Object, Object>> userProducts =
	  ratings.map(r -> new Tuple2<>(r.user(), r.product()));
	JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(
	  model.predict(JavaRDD.toRDD(userProducts)).toJavaRDD()
	      .map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating()))
	);
	JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD.fromJavaRDD(
	    ratings.map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating())))
	  .join(predictions).values();
	double MSE = ratesAndPreds.mapToDouble(pair -> {
	  double err = pair._1() - pair._2();
	  return err * err;
	}).mean();
	System.out.println(">>>>>>>>>>>>>>>Mean Squared Error = " + MSE);

	// Save and load model
/*	model.save(jsc.sc(), "target/tmp/myCollaborativeFilter");
	MatrixFactorizationModel sameModel = MatrixFactorizationModel.load(jsc.sc(),
	  "target/tmp/myCollaborativeFilter");
*/
	JavaRDD<Tuple2<Object, Rating[]>>  ratesAndPreds1 =  model.recommendProductsForUsers(10).toJavaRDD();
	
	JavaRDD<UserRecommendations> userRecommendationsRDD = ratesAndPreds1.map(tuple -> {
        Set<Integer> products = new HashSet();
        for (Rating rating : tuple._2) {
            products.add(rating.product());
        }

        return new UserRecommendations((int) tuple._1(), products);
    });
	
	List<UserRecommendations> lstRecomm = userRecommendationsRDD.collect();
	
	for(UserRecommendations user: lstRecomm) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>"+user.getUser()+">>>>>>>>>"+user.getProductSet());
	}
	/*System.out.println(">>>>>>>>>>>recommendation Array"+ratArray.length);
	for(Rating r: ratArray) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>"+r.product());
	}*/
	jsc.close();
}
}
