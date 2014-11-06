/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    Copyright (C) 2014 University of Alcala
 *
 */
package recommender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class RecommenderSamples
{
    public RecommenderSamples()
    {
        DBManager dBManager = new DBManager();

        dBManager.createDbFile(DBManager.Table.USER_RATINGS);
        dBManager.createDbFile(DBManager.Table.CONTENT_RATINGS);
    }

    public List recommendUsers(int idUser)
    {
        List recommendationList = new ArrayList();

        try
        {
            File ratingsFile = new File(DBManager.Table.USER_RATINGS.getTemporalFile());
            DataModel model = new FileDataModel(ratingsFile);

            // create a simple recommender on our data
            CachingRecommender cachingRecommender = new CachingRecommender(new SlopeOneRecommender(model));

            // get the recommendations for the user
            List<RecommendedItem> recommendations = cachingRecommender.recommend(idUser, 8);

            // print the list of recommendations for each 
            for (RecommendedItem recommendedItem : recommendations)
                recommendationList.add(recommendedItem.getItemID());
        }
        catch (TasteException | IOException ex)
        {
            Logger.getLogger(RecommenderSamples.class.getName()).log(Level.SEVERE, null, ex);
        }

        return recommendationList;
    }

    public List recommendUsersGeneric(int idUser)
    {
        List recommendationList = new ArrayList();

        try
        {
            File ratingsFile = new File(DBManager.Table.USER_RATINGS.getTemporalFile());
            DataModel model = new FileDataModel(ratingsFile);

            final UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            final UserNeighborhood neighborhood = new NearestNUserNeighborhood(25, similarity, model);

            // create a simple recommender on our data
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // get the recommendations for the user
            List<RecommendedItem> recommendations = recommender.recommend(idUser, 8);

            // print the list of recommendations for each 
            for (RecommendedItem recommendedItem : recommendations)
                recommendationList.add(recommendedItem.getItemID());
        }
        catch (TasteException | IOException ex)
        {
            Logger.getLogger(RecommenderSamples.class.getName()).log(Level.SEVERE, null, ex);
        }

        return recommendationList;
    }

    public List recommendContentWithUserid(int idUser)
    {
        List recommendationList = new ArrayList();

        try
        {
            File ratingsFile = new File(DBManager.Table.CONTENT_RATINGS.getTemporalFile());
            DataModel model = new FileDataModel(ratingsFile);

            ItemSimilarity sim = new LogLikelihoodSimilarity(model);
            Recommender recommender = new GenericItemBasedRecommender(model, sim);

            for (RecommendedItem recommendation : recommender.recommend(idUser, 8))
            {
                long reco = recommendation.getItemID();

                recommendationList.add(reco);
            }
        }
        catch (TasteException | IOException ex)
        {
            Logger.getLogger(RecommenderSamples.class.getName()).log(Level.SEVERE, null, ex);
        }

        return recommendationList;
    }

    public List recommendContentWithUseridGeneric(int idUser)
    {
        List recommendationList = new ArrayList();

        try
        {
            File ratingsFile = new File(DBManager.Table.CONTENT_RATINGS.getTemporalFile());
            DataModel model = new FileDataModel(ratingsFile);

            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(25, similarity, model);
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            for (RecommendedItem recommendation : recommender.recommend(idUser, 8))
            {
                long reco = recommendation.getItemID();

                recommendationList.add(reco);
            }
        }
        catch (TasteException | IOException ex)
        {
            Logger.getLogger(RecommenderSamples.class.getName()).log(Level.SEVERE, null, ex);
        }

        return recommendationList;
    }
}