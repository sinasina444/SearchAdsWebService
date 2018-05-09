package io.bittiger.ads;

import java.util.List;

public class AdsAllocation {
	private static AdsAllocation instance = null;
	private static double mainLinePriceThreshold = 4.5;
	protected AdsAllocation()
	{

	}
	public static AdsAllocation getInstance() {
	      if(instance == null) {
	         instance = new AdsAllocation();
	      }
	      return instance;
	}
	public void AllocateAds(List<Ad> ads)
	{
		for(Ad ad : ads)
		{
			if(ad.costPerClick >= mainLinePriceThreshold)
			{
				ad.position = 1;
			}
			else
			{
				ad.position = 2;
			}
		}
	}
}
