package src.a4;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import a4.Search;
import a4.Search.Vertex;


public class SearchTest {

	static Search mcgill_network;
	static ArrayList<Vertex> graph;
	
	@BeforeClass
	public static void initGraph()
	{
		String pathToVertices = "vertices.csv";
		String pathToEdges = "edges.csv";
				
		mcgill_network = new Search();
		mcgill_network.loadGraph(pathToVertices, pathToEdges);
		graph = mcgill_network.getGraph();
	}
		
	
	@Test
	public void GetNumberOfVerticesInGraph(){
		int size = 129;	
		assertTrue("The graph should have " + size + " vertices", size == mcgill_network.getGraphSize());	
	}
	
	@Test
	public void GetNumberOfEdgesInGraph(){
		int size = 912;
		
		int count = 0;
		for(Vertex k : graph){
			count += k.getNeighbors().size();
		}
		
		assertTrue("The graph should have " + size + " edges", size == count);	
	}
	
	@Test
	public void GetNeighborsOfVertex(){
		String url = "http://aoc.mcgill.ca/fr/accueil";
		String neighbors = ",http://www.mcgill.ca,http://aoc.mcgill.ca/,http://aoc.mcgill.ca/,http://aoc.mcgill.ca/fr/home-and-auto-insurance,http://aoc.mcgill.ca/fr/r%C3%A9seauter,http://aoc.mcgill.ca/fr/avantages-services,https://www.alumni.mcgill.ca/aoc/events-travel/registration/regwizEventsList.php?aoc=1,http://aoc.mcgill.ca/fr/benevolat,http://aoc.mcgill.ca/fr/donner";
	
		String results = "";
		for(Vertex k : graph){
			if(k.getURL().compareToIgnoreCase(url) == 0){
				for(Vertex v : k.getNeighbors()){
					results = results + "," + v.getURL();
				}
			}
		}
		
		assertTrue("Neighbors of this vertex are not correct: " + url, neighbors.compareTo(results)== 0);
		
	}
	
	@Test
	public void GetWordsOfVertex(){
		String url = "http://aoc.mcgill.ca/fr/accueil";
		String words = ",communaut,virtuelle,des,diplms,de,mcgill,aoc,mcgill,university,mcgill,university,chercher,dans,ce,site,english,aoc,main,navigation,home,and,auto,insurance,accueil,rseauter,avantages,services,vnements,bnvolat,donner,voyager,identifiant,mcgillca,aoc,section,navigation,communaut,virtuelle,des,diplms,de,mcgill,communaut,virtuelle,des,diplms,bienvenue,la,toute,nouvelle,version,franaise,de,la,communaut,virtuelle,des,anciens,tudiants,de,mcgill,ce,site,web,reprsente,officiellement,lassociation,des,diplms,de,mcgill,et,le,bureau,du,service,dveloppement,et,relations,avec,les,anciens,tudiants,de,luniversit,veuillez,noter,que,ce,site,tout,en,incluant,la,vaste,majorit,des,pages,qui,existent,en,anglais,est,toujours,en,dveloppement,et,nous,comptons,donc,y,ajouter,dautres,pages,dans,les,semaines,qui,suivent,plus,de,ans,dexcellence,et,dinnovation,depuis,la,cration,de,la,premire,socit,des,diplms,en,les,anciens,tudiants,et,amis,de,mcgill,mettent,profit,leur,crativit,pour,appuyer,luniversit,ils,ont,ainsi,contribu,la,croissance,de,lun,des,meilleurs,tablissements,denseignement,suprieur,au,monde,intgrant,des,technologies,de,pointe,les,meilleures,fonctionnalits,et,linformation,la,plus,facilement,accessible,notre,site,web,marque,un,nouveau,chapitre,de,cette,tradition,lhistoire,se,poursuit,titre,de,diplm,de,mcgill,vous,devenez,automatiquement,membre,de,lassociation,des,diplms,ainsi,que,lun,des,maillons,de,notre,illustre,histoire,nous,esprons,que,la,communaut,virtuelle,des,anciens,vous,incitera,renouer,avec,votre,alma,mater,vous,informer,au,sujet,des,services,que,nous,offrons,nous,parler,de,votre,vie,et,nous,faire,part,de,vos,ralisations,alors,parcourez,le,site,trouvez,une,faon,dapporter,votre,contribution,et,transmetteznous,vos,questions,english,related,content,as,a,mcgill,graduate,you,are,automatically,a,member,of,the,mcgill,alumni,association,and,a,part,of,our,illustrious,history,faire,un,don,chaque,don,compte,mcgill,est,de,nouveau,en,tte,pour,la,neuvime,anne,de,suite,le,magazine,macleans,dans,son,numro,annuel,sur,les,universits,canadiennes,accorde,la,premire,place,mcgill,parmi,les,universits,offrant,des,programmes,de,mdecine,et,de,doctorat,en,savoir,plus,department,and,university,information,column,column,column,column,column,column";
		
		String results = "";
		for(Vertex k : graph){
			if(k.getURL().compareToIgnoreCase(url) == 0){
				for(String s : k.getWords()){
					results = results + "," + s ;
				}
			}
		}
		
		assertTrue("Words on this page are not correct: " + url, words.compareTo(results)== 0);
	
	}

	@Test
	public void GetBFSTraversal(){
		mcgill_network.search("mcgill", "BFS");
		String inspector = mcgill_network.getBFSInspector();
		String correctFlow = ",http://www.mcgill.ca,http://www.mcgill.ca/home-page#main-column,http://www.mcgill.ca/home-page#nav,https://www.alumni.mcgill.ca/aoc/online-giving/step1.php?new=1,https://exchange.mcgill.ca/,http://www.mcgill.ca/minerva,https://mycourses2.mcgill.ca/Shibboleth.sso/Login,https://mymcgill.mcgill.ca,http://www.mcgill.ca/maps,http://www.mcgill.ca/directory/staff,http://www.mcgill.ca/,http://aoc.mcgill.ca,http://aoc.mcgill.ca/home,http://aoc.mcgill.ca/network,http://aoc.mcgill.ca/benefits-services,https://www.alumni.mcgill.ca/aoc/events-travel,http://aoc.mcgill.ca/volunteer,http://aoc.mcgill.ca/give,http://aoc.mcgill.ca/travel,http://www.microsoft.com/windows/ie/downloads/default.mspx,https://exchange.mcgill.ca/owa/auth/logon.aspx?url=https://exchange.mcgill.ca/owa/&reason=0#,http://go.microsoft.com/fwlink/?LinkID=129362,http://kb.mcgill.ca/it/mcgillusername,http://www.mcgill.ca/forgot-password,https://horizon.mcgill.ca/pban1/twbkwbis.P_WWWLogin#main_content,https://horizon.mcgill.ca/fr-PBAN1/twbkwbis.P_WWWLogin,http://www.is.mcgill.ca/whelp/gen_help/login.htm,https://horizon.mcgill.ca/pban1/twbkwbis.P_Logout,https://horizon.mcgill.ca/pban1/bzgkemlv.p_forgot_id,https://horizon.mcgill.ca/pban1/twbkwbis.P_WWWLogin#,http://www.mcgill.ca/secretariat/policies/informationtechnology,https://horizon.mcgill.ca/pban1/twbkwbis.P_WWWLogin#top,http://www.mcgill.ca/about/,http://aoc.mcgill.ca/fr,http://aoc.mcgill.ca/,https://www.alumni.mcgill.ca/aoc/events-travel/registration/regwizEventsList.php?aoc=1,http://aoc.mcgill.ca/fr/r%C3%A9seauter,http://aoc.mcgill.ca/fr/avantages-services,http://aoc.mcgill.ca/fr/benevolat,http://aoc.mcgill.ca/fr/donner,http://aoc.mcgill.ca/fr/voyager,http://windows.microsoft.com/en-us/windows/home,http://windows.microsoft.com:80/en-us/internet-explorer/download-ie#bodyContentPane,http://windows.microsoft.com/en-us/windows-8/why-windows,http://windows.microsoft.com/en-us/windows-8/apps,http://windows.microsoft.com/en-us/windows-8/windows-pcs,http://windows.microsoft.com/en-us/windows/downloads,http://windows.microsoft.com/en-us/windows/support,https://account.live.com/summarypage.aspx,http://login.live.com:80/logout.srf?rver=6.1.6206.0&lc=1033&id=285275&ru=https%3a%2f%2fredir.windows.microsoft.com%2fliveid%3fru%3den-us%252finternet-explorer%252fdownload-ie,http://help.outlook.com/en-us/140/default(d=loband).aspx,http://help.outlook.com/id-id/140/bb899685(d=loband).aspx,http://help.outlook.com/ms-my/140/bb899685(d=loband).aspx,http://help.outlook.com/ca-es/140/bb899685(d=loband).aspx,http://help.outlook.com/de-de/140/bb899685(d=loband).aspx,http://help.outlook.com/en-au/140/bb899685(d=loband).aspx,http://help.outlook.com/en-ca/140/bb899685(d=loband).aspx,http://help.outlook.com/en-in/140/bb899685(d=loband).aspx,http://help.outlook.com/en-gb/140/bb899685(d=loband).aspx,http://help.outlook.com/en-us/140/bb899685(d=loband).aspx,https://horizon.mcgill.ca/fr-PBAN1/twbkwbis.P_WWWLogin#main_content,https://horizon.mcgill.ca/pban1/twbkwbis.P_WWWLogin,http://www.is.mcgill.ca/whelp/gen_help/login.htm#french,https://horizon.mcgill.ca/fr-PBAN1/twbkwbis.P_Logout,https://horizon.mcgill.ca/fr-PBAN1/bzgkemlv.p_forgot_id,https://horizon.mcgill.ca/fr-PBAN1/twbkwbis.P_WWWLogin#,https://horizon.mcgill.ca/fr-PBAN1/twbkwbis.P_WWWLogin#top,http://kb.mcgill.ca/it/easylink/article.html?id=1431,http://kb.mcgill.ca/it/servicedesk,http://www.is.mcgill.ca/whelp/gen_help/login.htm#references,http://www.is.mcgill.ca/whelp/gen_help/login.htm#loginFirstTime,http://www.is.mcgill.ca/whelp/gen_help/login.htm#forgetID,http://www.is.mcgill.ca/whelp/gen_help/login.htm#forgetPIN,http://www.is.mcgill.ca/whelp/gen_help/login.htm#forgetPwd,http://www.is.mcgill.ca/whelp/gen_help/login.htm#changePIN_Pwd,http://www.is.mcgill.ca/whelp/gen_help/login.htm#before2002,https://horizon.mcgill.ca/pban1/twbkwbis.P_Logout#main_content,http://www.is.mcgill.ca/whelp/gen_help/default_help.htm,https://horizon.mcgill.ca/pban1/twbkwbis.P_Logout#top,https://horizon.mcgill.ca/pban1/bzgkemlv.p_forgot_id#main_content,http://www.is.mcgill.ca/whelp/gen_help/forgot_id.htm,https://horizon.mcgill.ca/pban1/bzgkemlv.p_forgot_id#top,http://aoc.mcgill.ca/fr/home-and-auto-insurance,http://aoc.mcgill.ca/fr/accueil,http://windows.microsoft.com/en-us/windows/home#bodyContentPane,http://login.live.com:80/logout.srf?rver=6.1.6206.0&lc=1033&id=285275&ru=https%3a%2f%2fredir.windows.microsoft.com%2fliveid%3fru%3den-us%252fwindows%252fhome,http://windows.microsoft.com/en-us/windows-8/why-windows#bodyContentPane,http://login.live.com:80/logout.srf?rver=6.1.6206.0&lc=1033&id=285275&ru=https%3a%2f%2fredir.windows.microsoft.com%2fliveid%3fru%3den-us%252fwindows-8%252fwhy-windows,http://windows.microsoft.com/en-us/windows-8/apps#bodyContentPane,http://login.live.com:80/logout.srf?rver=6.1.6206.0&lc=1033&id=285275&ru=https%3a%2f%2fredir.windows.microsoft.com%2fliveid%3fru%3den-us%252fwindows-8%252fapps,http://windows.microsoft.com/en-us/windows-8/windows-pcs#bodyContentPane,http://login.live.com:80/logout.srf?rver=6.1.6206.0&lc=1033&id=285275&ru=https%3a%2f%2fredir.windows.microsoft.com%2fliveid%3fru%3den-us%252fwindows-8%252fwindows-pcs,http://windows.microsoft.com/en-us/windows/downloads#bodyContentPane,http://login.live.com:80/logout.srf?rver=6.1.6206.0&lc=1033&id=285275&ru=https%3a%2f%2fredir.windows.microsoft.com%2fliveid%3fru%3den-us%252fwindows%252fdownloads,http://windows.microsoft.com/en-us/windows/support#bodyContentPane,http://login.live.com:80/logout.srf?rver=6.1.6206.0&lc=1033&id=285275&ru=https%3a%2f%2fredir.windows.microsoft.com%2fliveid%3fru%3den-us%252fwindows%252fsupport,http://help.outlook.com/id-id/140/cc325690(d=loband).aspx,http://help.outlook.com/ms-my/140/cc325690(d=loband).aspx,http://help.outlook.com/ca-es/140/cc325690(d=loband).aspx,http://help.outlook.com/de-de/140/cc325690(d=loband).aspx,http://help.outlook.com/en-au/140/cc325690(d=loband).aspx,http://help.outlook.com/en-ca/140/cc325690(d=loband).aspx,http://help.outlook.com/en-in/140/cc325690(d=loband).aspx,http://help.outlook.com/en-gb/140/cc325690(d=loband).aspx,http://help.outlook.com/en-us/140/cc325690(d=loband).aspx,http://help.outlook.com/id-id/140/default(d=loband).aspx,http://help.outlook.com/ms-my/140/default(d=loband).aspx,http://help.outlook.com/ca-es/140/default(d=loband).aspx,http://help.outlook.com/de-de/140/default(d=loband).aspx,http://help.outlook.com/en-au/140/default(d=loband).aspx,http://help.outlook.com/en-ca/140/default(d=loband).aspx,http://help.outlook.com/en-in/140/default(d=loband).aspx,http://help.outlook.com/en-gb/140/default(d=loband).aspx,https://horizon.mcgill.ca/fr-PBAN1/twbkwbis.P_Logout#main_content,https://horizon.mcgill.ca/fr-PBAN1/twbkwbis.P_Logout#top,https://horizon.mcgill.ca/fr-PBAN1/bzgkemlv.p_forgot_id#main_content,http://www.is.mcgill.ca/whelp/gen_help/forgot_id.htm#french,https://horizon.mcgill.ca/fr-PBAN1/bzgkemlv.p_forgot_id#top,http://www.is.mcgill.ca/whelp/gen_help/default_help.htm#FRENCH,http://kb.mcgill.ca/it/minerva-students,http://kb.mcgill.ca/it/minerva-faculty-staff,http://www.mcgill.ca/minerva-guests,http://www.is.mcgill.ca/whelp/gen_help/search_help.htm,http://www.is.mcgill.ca/whelp/gen_help/default_help.htm#,http://www.mcgill.ca/students/servicepoint,http://www.is.mcgill.ca/whelp/gen_help/forgot_id.htm#,http://aoc.mcgill.ca/benefits-services/insurance/home-and-auto-insurance,http://www.is.mcgill.ca/whelp/gen_help/search_help.htm#FRENCH,http://www.is.mcgill.ca/whelp/gen_help/search_help.htm#";
		
		assertTrue("Your BFS Algorithm doesn not perform BFS correctly", inspector.compareTo(correctFlow)== 0);
		
	}
	
	@Test
	public void getDFSResults() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String keyword = "mcgill";
		int results = 75;
	
		mcgill_network.search(keyword, "DFS");
		int searches = mcgill_network.displaySearchResults();
		
		assertTrue("The search results for the keyword '"+keyword+"' are not correct. Expected "+results+" results and found only "+searches, results == searches);
	
	}
	
	
	
	
	
}
