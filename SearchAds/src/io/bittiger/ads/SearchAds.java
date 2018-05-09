package io.bittiger.ads;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Servlet implementation class SearchAds
 */
@WebServlet("/SearchAds")
public class SearchAds extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletConfig config = null;  
	private AdsEngine adsEngine = null;
	private String uiTemplate = "";
	private String adTemplate = "";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchAds() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		this.config =  config;
		super.init(config);
		ServletContext application = config.getServletContext();
	    String adsDataFilePath = application.getInitParameter("adsDataFilePath");
	    String budgetDataFilePath = application.getInitParameter("budgetDataFilePath");
	    String uiTemplateFilePath = application.getInitParameter("uiTemplateFilePath");
	    String adTemplateFilePath = application.getInitParameter("adTemplateFilePath");
	    String memcachedServer = application.getInitParameter("memcachedServer");
	    String mysqlHost = application.getInitParameter("mysqlHost");
	    String mysqlDb = application.getInitParameter("mysqlDB");
	    String mysqlUser = application.getInitParameter("mysqlUser");
	    String mysqlPass = application.getInitParameter("mysqlPass");
	    int memcachedPortal = Integer.parseInt(application.getInitParameter("memcachedPortal"));
		this.adsEngine = new AdsEngine(adsDataFilePath,budgetDataFilePath,memcachedServer,memcachedPortal,mysqlHost,mysqlDb,mysqlUser,mysqlPass);
		this.adsEngine.init();  
		System.out.println("adsEngine initilized");
		//load UI template
		try {
			byte[] uiData;
			byte[] adData;
			uiData = Files.readAllBytes(Paths.get(uiTemplateFilePath));
			uiTemplate = new String(uiData, StandardCharsets.UTF_8);
			adData = Files.readAllBytes(Paths.get(adTemplateFilePath));
			adTemplate = new String(adData, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("UI template initilized");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String query = request.getParameter("q");
		List<Ad> adsCandidates = adsEngine.selectAds(query);
		String result = uiTemplate;
        String list = "";
		for(Ad ad : adsCandidates)
		{
			System.out.println("final selected ad id = " + ad.adId);
			System.out.println("final selected ad rank score = " + ad.rankScore);
			String adContent = adTemplate;
			adContent = adContent.replace("$title$", ad.title);
			adContent = adContent.replace("$brand$", ad.brand);
			adContent = adContent.replace("$img$", ad.thumbnail);
			adContent = adContent.replace("$link$", ad.detail_url);
			adContent = adContent.replace("$price$", Double.toString(ad.price));
			//System.out.println("adContent: " + adContent);
			list = list + adContent;
		}
		result = result.replace("$list$", list);
		//System.out.println("list: " + list);
		//System.out.println("RESULT: " + result);
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(result);	
	}
}
