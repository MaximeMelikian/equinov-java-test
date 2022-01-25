package com.eqinov.recrutement.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eqinov.recrutement.consumption.ConsumptionDispatcher;
import com.eqinov.recrutement.data.DataPoint;
import com.eqinov.recrutement.data.Site;
import com.eqinov.recrutement.repository.DataPointRepository;
import com.eqinov.recrutement.repository.SiteRepository;
import com.eqinov.recrutement.utils.DateUtils;

/**
 * Controller Spring permettant l'affichage des donn�es dans la seule vue de
 * l'application
 * 
 * @author Guillaume SIMON - EQINOV
 * @since 27 janv. 2020
 *
 */
@Controller
public class WelcomeController {

	@Autowired
	private SiteRepository siteRepository;

	@Autowired
	private DataPointRepository dataPointRepository;

	/**
	 * Point d'entr�e de la vue, page d'accueil de l'application
	 */
	@GetMapping("/")
	public String main(Model model) {
		Optional<Site> site = siteRepository.findById(1l);
		if (site.isPresent()) {
			Integer maxYear = dataPointRepository.findTopBySiteOrderByTimeDesc(site.get()).getTime().getYear();
			initModel(site.get(), maxYear, model);
		}
		return "welcome";
	}

	/**
	 * Rafraichi le contenu de la page sur changement d'ann�e
	 * 
	 * @param year  l'ann�e
	 * @param model model transportant les donn�es
	 * @return le fragment a retourn�
	 */
	@GetMapping("/view/refresh")
	public String refresh(@RequestParam Integer year, Model model) {
		Optional<Site> site = siteRepository.findById(1l);
		if (site.isPresent()) {
			initModel(site.get(), year, model);
		}
		return "welcome:: result";
	}

	/**
	 * M�thode interne permettant d'ajouter les donn�es du site pour l'ann�e �
	 * afficher
	 * 
	 * @param site        site � afficher
	 * @param currentYear ann�e s�lectionn�e
	 * @param model       model transportant les donn�es
	 */
	private void initModel(Site site, Integer currentYear, Model model) {
		Integer minYear = dataPointRepository.findTopBySiteOrderByTimeAsc(site).getTime().getYear();
		Integer maxYear = dataPointRepository.findTopBySiteOrderByTimeDesc(site).getTime().getYear();
		List<Integer> years = Stream.iterate(minYear, n -> n + 1).limit((maxYear - minYear) + 1l).map(n -> n)
				.collect(Collectors.toList());
		
		ConsumptionDispatcher dispatcher = buildConsumptionDispatcher(site, currentYear);
		
		model.addAttribute("years", years);
		model.addAttribute("currentYear", currentYear);
		model.addAttribute("site", site);
		model.addAttribute("dispatcher", dispatcher);
	}

	/**
	 * Retourne les points de consommation d'une ann�e au format json pour highstock
	 * 
	 * @param year ann�e
	 * @return
	 */
	@GetMapping(value = "/data/conso", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<double[]> getConso(@RequestParam Integer year) {
		Optional<Site> site = siteRepository.findById(1l);
		List<double[]> result = new ArrayList<>();
		if (site.isPresent()) {
			List<DataPoint> yearPoints = extractYearPoints(site.get(), year);
			result = yearPoints.stream().map(point -> {
				double[] array = new double[2];
				array[0] = DateUtils.secondsFromEpoch(point.getTime()) * 1000l;
				array[1] = point.getValue();
				return array;
			}).collect(Collectors.toList());
		}
		return result;
	}
	
	/**
	 * 
	 * Construit un Consumption dispatcher avec les consommations pour chaque mois
	 * 
	 * @param site
	 * @param currentYear
	 * @return
	 */
	public ConsumptionDispatcher buildConsumptionDispatcher(Site site, int currentYear) {
		List<List<DataPoint>> yearPoints = new ArrayList<List<DataPoint>>();
		for(int i=1; i<=12 ; i++) {
			yearPoints.add(extractMonthPoints(site, currentYear, i));
		}
		return new ConsumptionDispatcher(currentYear, yearPoints);
	}
	
	/**
	 * 
	 * Extrait les données de consommation pour le mois selectionné
	 * 
	 * @param site
	 * @param year
	 * @param month
	 * @return
	 */
	private List<DataPoint> extractMonthPoints(Site site, int year, int month) {
		LocalDateTime start = LocalDate.of(year, month, 1).atStartOfDay();
		LocalDateTime end = start.plusMonths(1).minusMinutes(1);
		return dataPointRepository.findBySiteAndTimeBetween(site, start, end);
	}
	
	/**
	 * 
	 * Extrait les données de consommation pour l'année selectionnée
	 * 
	 * @param site
	 * @param year
	 * @return
	 */
	private List<DataPoint> extractYearPoints(Site site, int year) {
		return dataPointRepository.findBySiteAndTimeBetween(site,
				LocalDate.of(year, 1, 1).atStartOfDay(),
				LocalDate.of(year, 12, 31).atStartOfDay().with(LocalTime.MAX));
	}

}