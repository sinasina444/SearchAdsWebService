package io.bittiger.ads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class AdsCampaignManager {
	private static AdsCampaignManager instance = null;
	private String mysql_host;
	private String mysql_db;
	private String mysql_user;
	private String mysql_pass;
	private static double minPriceThreshold = 0.0;
	private MySQLAccess mysql;
	protected AdsCampaignManager(String mysqlHost,String mysqlDb,String user,String pass)
	{
		mysql_host = mysqlHost;
		mysql_db = mysqlDb;	
		mysql_user = user;
		mysql_pass = pass;	
		mysql = new MySQLAccess(mysql_host, mysql_db, mysql_user, mysql_pass);

	}
	public static AdsCampaignManager getInstance(String mysqlHost,String mysqlDb,String user,String pass) {
	      if(instance == null) {
	         instance = new AdsCampaignManager(mysqlHost,mysqlDb,user,pass);
	      }
	      return instance;
	}
	public  List<Ad> DedupeByCampaignId(List<Ad> adsCandidates)
	{
		List<Ad> dedupedAds = new ArrayList<Ad>();
		HashSet<Long> campaignIdSet = new HashSet<Long>();
		for(Ad ad : adsCandidates)
		{
			if(!campaignIdSet.contains(ad.campaignId))
			{
				dedupedAds.add(ad);
				campaignIdSet.add(ad.campaignId);
			}
		}
		return dedupedAds;
	}
	public List<Ad> ApplyBudget(List<Ad> adsCandidates)
	{
		List<Ad> ads = new ArrayList<Ad>();
		try
		{
			for(int i = 0; i < adsCandidates.size()  - 1;i++)
			{
				Ad ad = adsCandidates.get(i);
				Long campaignId = ad.campaignId;
				System.out.println("campaignId: " + campaignId);
				Double budget = mysql.getBudget(campaignId);
				System.out.println("AdsCampaignManager ad.costPerClick= " + ad.costPerClick);
				System.out.println("AdsCampaignManager campaignId= " + campaignId);
				System.out.println("AdsCampaignManager budget left = " + budget);

				if(ad.costPerClick <= budget && ad.costPerClick >= minPriceThreshold)
				{
					ads.add(ad);
					budget = budget - ad.costPerClick;					
					mysql.updateCampaignData(campaignId, budget);
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ads;
	}
	
}
