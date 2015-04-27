package br.com.escritorio.advocacia.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class DataUtil {
	public static final SimpleDateFormat sdf_DDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
	public static final SimpleDateFormat sdf_DDMMYYYY_HH = new SimpleDateFormat("dd/MM/yyyy HH");
	public static final SimpleDateFormat sdf_DDMMYYYY_HHMMSS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public static final SimpleDateFormat sdf_MM_DD_YYYY_HHMMSS = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	public static final SimpleDateFormat sdf_DD_MM_YYYY_HHMMSS = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	public static final SimpleDateFormat sdf_MMYYYY = new SimpleDateFormat("MM/yyyy");
	
	public static String getHoje() {
		return sdf_DDMMYYYY.format(new Date());
	}
	
	public static String getOntem() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return sdf_DDMMYYYY.format(cal.getTime());
	}
	
	/**
	 * <p> Retorna uma data alterada de acordo com o campo e qtd passada. </p>
	 * Exemplo: <br/>
	 * <code>DataUtil.getDataAlterada(Calendar.DAY_OF_YEAR, -2) </code> <br/><br/>
	 * Retorna a data de hoje menos dois dias (d-2) <br/>
	 * @param campo usar Calendar
	 * @param qtd
	 * @return
	 */
	public static String getDataAlterada(int campo, int qtd) {
		Calendar cal = Calendar.getInstance();
		cal.add(campo, qtd);
		return sdf_DDMMYYYY.format(cal.getTime());
	}
	
	public static String getDataAlterada(int campo, int qtd, SimpleDateFormat sdf) {
		Calendar cal = Calendar.getInstance();
		cal.add(campo, qtd);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Método para parsear uma data em um simple date format pré-definido com controle de sincronização, já que DateFormat não é safe thread
	 * @param data String contendo a data
	 * @param sdf Quaquer tipo de formatacao
	 * @return Data formatada
	 */
	public synchronized static Date parse(String data, SimpleDateFormat sdf) {
		Date dataRetorno = null;
		try {
			dataRetorno = sdf.parse(data);
		} catch (ParseException e) {
		}
		return dataRetorno;
	}
	
	/**
	 * Método para formatar uma data em um simple date format pré-definido com controle de sincronização, já que DateFormat não é safe thread
	 * @param data String contendo a data
	 * @param sdf Quaquer tipo de formatacao
	 * @return Data formatada
	 */
	public synchronized static String format(Date data, SimpleDateFormat sdf) {
		String dataRetorno = null;
		dataRetorno = sdf.format(data);
		return dataRetorno;
	}
	
	/**
	 * Metodo converter de lista de string para lista de datas
	 * @param datas Lista contendo as datas no formata String
	 * @return Lista de datas no formato DD/MM/YYYY
	 */
	public static List<Date> criarListaDatas(List<String> datas){
		List<Date> datasRetorno = new ArrayList<Date>();
		for (String strData : datas) {
			datasRetorno.add(formataDataDefault(strData));
		}
		return datasRetorno;
	}
	
	/**
	 * Metodo para criar uma lista de datas baseada na data inicio e fim
	 * <p>
	 * 	EX>.: Se for passado 11/04/2013 , 16/04/2013 o metodo ira retornar uma lista de datas contendo
	 *  {11/04/2013 , 12/04/2013 , 13/04/2013 , 14/04/2013 , 15/04/2013 , 16/04/2013}
	 * </p>
	 * @param dataInicial Data inicial da contagem
	 * @param dataFinal Data final da contagem
	 * @return Lista de datas entre o intervalo das datas passadas
	 */
	public static List<String> criarListaDatas(String dataInicio, String dataFim){
		Date dataInicioDate = zerarHorasMinSecMilisec(formataDataDefault(dataInicio));
		Date dataFimDate = zerarHorasMinSecMilisec(formataDataDefault(dataFim));
		if(dataFimDate.getTime() < dataInicioDate.getTime()){
			Date dataAux = dataInicioDate;
			dataInicioDate = dataFimDate;
			dataFimDate = dataAux;
		}
		Long dif = diferencaEntreDatas(dataInicioDate, dataFimDate);
		List<String> datasRetorno = new ArrayList<String>();
		datasRetorno.add(dataInicio);
		if(dif != 0){
			Calendar contador = Calendar.getInstance();
			contador.setTime(dataInicioDate);
			for (int i = 0; i < dif.intValue(); i++) {
				contador.add(Calendar.DAY_OF_MONTH, 1);
				datasRetorno.add(sdf_DDMMYYYY.format(contador.getTime()));
			}
		}
		return datasRetorno;
	}
	
	/**
	 * Metodo para calcular a diferenca em dias entre duas datas 
	 * @param dataInicial Data inicio
	 * @param dataFinal Data fim
	 * @return a diferenca em dias das duas datas
	 */
	public static Long diferencaEntreDatas(Date dataInicial, Date dataFinal){
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(dataInicial);
        
        Calendar fim = Calendar.getInstance();
        fim.setTime(dataFinal);
 
        long diferenca = fim.getTimeInMillis() - inicio.getTimeInMillis();
        int tempoDia = 1000 * 60 * 60 * 24;
		return diferenca / tempoDia;
	}
	
	/**
	 * Metodo para formatar a data de forma default (DD/MM/YYYY)
	 * @param data String contendo a data para ser formatada na padrao dd/mm/yyyy
	 * @return Date formatada
	 */
	public static Date formataDataDefault(String data){
		Date dataRetorno = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataRetorno = sdf.parse(data);
		} catch (ParseException e) {
//			Log.debug(e.getMessage());
		}
		return dataRetorno;
	}
	
	/**
	 * Metodo para formatar a data de forma default (DD/MM/YYYY)
	 * @param data String contendo a data para ser formatada na padrao dd/mm/yyyy
	 * @return Date formatada
	 */
	public static Date formataDataDefault(String data, String formato){
		Date dataRetorno = null;
		try {
			dataRetorno = new SimpleDateFormat(formato).parse(data);
		} catch (ParseException e) {
//			Log.debug(e.getMessage());
		}
		return dataRetorno;
	}
	
	/**
	 * Metodo para formatar a data do banco para ser utilizada dentro da aplicacao/apresentada para o usuario
	 * @param data String contendo a data para ser formatada
	 * @return String contendo a data formatada
	 */
	public static String formataDataView(String data) {
		String dataFormatada = null;
		try {
			dataFormatada = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(data));
		} catch (ParseException e) {
//			Log.debug(e.getMessage());
		}
		return dataFormatada;
	}

	/**
	 * Metodo para formatar a data do banco para ser utilizada dentro da aplicacao/apresentada para o usuario
	 * @param data String contendo a data para ser formatada
	 * @return Data formatada
	 */
	public static Date formataDataView(Date data) {
		String dataFormatada = null;
		dataFormatada = new SimpleDateFormat("dd/MM/yyyy").format(data);
		return formataDataDefault(dataFormatada);
	}
	
	/**
	 * Metodo para retornar a data corrente
	 * @return Dia corrente(atual)
	 */
	public static  Date getDataCorrente() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0 , 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Metodo para formatar uma data de acordo com a formatacao passada
	 * @param data Data para ser formatada
	 * @param formato Formato da data
	 * @return String contendo o novo formato da data
	 */
	public static String formataData(Date data, SimpleDateFormat formato) {
		return formato.format(data);
	}

	/**
	 * Metodo para verificar se uma data é valida
	 * @param data Data para ser validada
	 * @return se a data é valida ou nao
	 */
	public static boolean isDataValida(Date data) {
		String dataVerificacao = formataData(data, new SimpleDateFormat("dd/MM/yyyy"));
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
		Boolean isDataValida = Boolean.FALSE;
		try {  
			format.setLenient(false);  
			data = format.parse(dataVerificacao);
			isDataValida = Boolean.TRUE;
		} catch (ParseException e) {  
//			Log.debug(e.getMessage());
		}  
		return isDataValida;
	}

	/**
	 * Metodo para subtrair uma quantidade de dias de uma data
	 * @param data Data para ser alterada
	 * @param dia Quantidade de dias para ser subtraido da data
	 * @return Data descrecida da quantidade do dia passado
	 */
	public static Date subtrairDiaData(Date data , Integer dia) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		Integer qtdDia = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, qtdDia - dia);
		return calendar.getTime();
	}
	
	/** Método para retornar a data atual com uma hora especificada. Será utilizada para disparar alguns jobs, como, por exemplo o  job de Filtros dinâmicos que precisam ser executado em uma hora específica.
	 * @param hora Hora do dia a ser considerada
	 * @return data atual com a hora especificada
	 */
	public static Date dataPorHora(int hora){
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());
		gc.set(GregorianCalendar.HOUR_OF_DAY,hora);
		gc.set(GregorianCalendar.MINUTE,0);
		gc.set(GregorianCalendar.SECOND,0);
		
		return gc.getTime();
	}
	
	/**
	 * Metodo para zerar as horas, minutos, segundos e milisegundos de uma data qualquer
	 * @param data Data que sera zerada
	 * @return Date com as horas, minutos, segundos e milisegundos 
	 */
	public static Date zerarHorasMinSecMilisec (Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR, 1);
	    calendar.set(Calendar.MINUTE, 0);  
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT-3"));
	    return calendar.getTime();
	}
}
