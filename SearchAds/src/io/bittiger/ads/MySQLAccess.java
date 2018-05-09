package io.bittiger.ads;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MySQLAccess {
	 private Connection d_connect = null; 
     private String d_user_name;
     private String d_password;
     private String d_server_name;
     private String d_db_name;
     
     public void close() throws Exception {
         System.out.println("Close database");
           try {
             if (d_connect != null) {
                 d_connect.close();
             }
           } catch (Exception e) {
               throw e;
           }
    }
      
     public MySQLAccess(String server,String db,String user,String password) {
         d_server_name = server;
         d_db_name = db;
         d_user_name = user;
         d_password = password;
         try {
			getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     public void getConnection() throws Exception {
           try {
        	   // This will load the MySQL driver, each DB has its own driver
               Class.forName("com.mysql.jdbc.Driver");
               String conn = "jdbc:mysql://" + d_server_name + "/" + 
                       d_db_name+"?user="+d_user_name+"&password="+d_password;
               System.out.println("Connecting to database: " + conn);
               d_connect = DriverManager.getConnection(conn);
               System.out.println("Connected to database");
           } catch(Exception e) {
               throw e;
           }     
      }
      
     private Boolean isRecordExist(String sql_string) throws SQLException {
    	 PreparedStatement existStatement = null;
    	 boolean isExist = false;

    	 try
         {
        	 existStatement = d_connect.prepareStatement(sql_string);
             ResultSet result_set = existStatement.executeQuery();
             if (result_set.next())
             {
            	 isExist = true;
             }  
         }
         catch(SQLException e )
          {
                   System.out.println(e.getMessage());
                   throw e;
          } 
          finally
          {
               if (existStatement != null)
               {
                   existStatement.close();
               };
         }
    	  
         return isExist;
     }
     
     public void addAdData(Ad ad) throws Exception {
    	 boolean isExist = false;
    	 String sql_string = "select adId from " + d_db_name + ".ad where adId=" + ad.adId;
    	 PreparedStatement ad_info = null;
    	 try
         {
        	 isExist = isRecordExist(sql_string);        
         }
         catch(SQLException e )
          {
              System.out.println(e.getMessage());
              throw e;
          } 
    	 
    	 if(isExist) {
    		 return;
    	 }
    	 
         sql_string = "insert into " + d_db_name +".ad values(?,?,?,?,?,?,?,?,?,?,?)";
          try {
        	  ad_info = d_connect.prepareStatement(sql_string);
        	  ad_info.setLong(1, ad.adId);
        	  ad_info.setLong(2, ad.campaignId);
        	  String keyWords = Utility.strJoin(ad.keyWords, ",");			  
        	  ad_info.setString(3, keyWords);
        	  ad_info.setDouble(4, ad.bidPrice);
        	  ad_info.setDouble(5, ad.price);
        	  ad_info.setString(6, ad.thumbnail);
        	  ad_info.setString(7, ad.description);
        	  ad_info.setString(8, ad.brand);
        	  ad_info.setString(9, ad.detail_url);
        	  ad_info.setString(10, ad.category);
        	  ad_info.setString(11, ad.title);
        	  ad_info.executeUpdate();
          }
          catch(SQLException e )
          {
                   System.out.println(e.getMessage());
                   throw e;
          } 
          finally
          {
               if (ad_info != null) {
            	   ad_info.close();
               };
         }   	 
     }
     public Ad getAdData(Long adId) throws Exception {
    	 
    	 PreparedStatement adStatement = null;
    	 ResultSet result_set = null;
    	 Ad ad = new Ad();
    	 String sql_string = "select * from " + d_db_name + ".ad where adId=" + adId;
    	 try {
        	 adStatement = d_connect.prepareStatement(sql_string);
        	 result_set = adStatement.executeQuery();
             while (result_set.next()) {      	 
            	 ad.adId = result_set.getLong("adId");
            	 ad.campaignId = result_set.getLong("campaignId");
            	 String keyWords = result_set.getString("keyWords");
            	 String[] keyWordsList = keyWords.split(",");
            	 ad.keyWords = Arrays.asList(keyWordsList);
            	 ad.bidPrice = result_set.getDouble("bidPrice");
            	 ad.price = result_set.getDouble("price");
            	 ad.thumbnail = result_set.getString("thumbnail");
            	 ad.description = result_set.getString("description");
            	 ad.brand = result_set.getString("brand");
            	 ad.detail_url = result_set.getString("detail_url");
            	 ad.category = result_set.getString("category");
            	 ad.title = result_set.getString("title");
             }  
         }
    	 catch(SQLException e )
         {
                  System.out.println(e.getMessage());
                  throw e;
         } 
         finally
         {
              if (adStatement != null) {
            	  adStatement.close();
              };
              if (result_set != null) {
            	  result_set.close();
              }
        }  
    	return ad;
     }
     
     public void addCampaignData(Campaign campaign) throws Exception {
    	 Connection connect = null;
    	 boolean isExist = false;
    	 String sql_string = "select campaignId from " + d_db_name + ".campaign where campaignId=" + campaign.campaignId;
    	 try
         {
        	 isExist = isRecordExist(sql_string);        
         }
         catch(SQLException e )
          {
              System.out.println(e.getMessage());
              throw e;
          } 
          finally
          {
               if(connect != null && isExist) {
            	   connect.close();
               }
         }
  	     if(isExist) {
  	    	 return;
  	     }
    	 PreparedStatement camp_info = null;   	 
         sql_string = "insert into " + d_db_name +".campaign values(?,?)";
         try {
        	  camp_info = connect.prepareStatement(sql_string);
        	  camp_info.setLong(1, campaign.campaignId);
			  camp_info.setDouble(2, campaign.budget);
			  camp_info.executeUpdate();
         }
         catch(SQLException e )
         {
              System.out.println(e.getMessage());
              throw e;
         } 
         finally
         {
              if (camp_info != null) {
            	  camp_info.close();
              };
              if (connect != null) {
           	   connect.close();          	   
              }
        }   	 
     }
     public Double getBudget(Long campaignId)  throws Exception {
    	 PreparedStatement selectStatement = null;
    	 ResultSet result_set = null;
    	 Double budget = 0.0;
    	 String sql_string= "select budget from " + d_db_name + ".campaign where campaignId=" + campaignId;
         System.out.println("sql: " + sql_string);
    	 try
         {
        	 selectStatement = d_connect.prepareStatement(sql_string);
        	 result_set = selectStatement.executeQuery();
             while (result_set.next()) { 
            	 budget = result_set.getDouble("budget");
             }
         }
         catch(SQLException e )
          {
              System.out.println(e.getMessage());
              throw e;
          } 
          finally
          {
        	   if(selectStatement != null) {
        		   selectStatement.close();
        	   }
        	   if(result_set != null) {
        		   result_set.close();
        	   }
         }
    	 return budget;
     }
     public void updateCampaignData(Long campaignId,Double newBudget) throws Exception {
    	 PreparedStatement updateStatement = null;
    	 String sql_string= "update " + d_db_name + ".campaign set budget=" + newBudget +" where campaignId=" + campaignId;
    	 System.out.println("sql: " + sql_string);
    	 try
         {
        	 updateStatement = d_connect.prepareStatement(sql_string);
        	 updateStatement.executeUpdate();
         }
         catch(SQLException e )
          {
              System.out.println(e.getMessage());
              throw e;
          } 
          finally
          {
        	   if(updateStatement != null) {
        		   updateStatement.close();
        	   }
         }
    	 
     }
}
