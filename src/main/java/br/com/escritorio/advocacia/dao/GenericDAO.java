package br.com.escritorio.advocacia.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import Annotation.SearchConfig;
import Annotation.SearchType;
import br.com.escritorio.advocacia.entidade.Entidade;
import br.com.escritorio.advocacia.exception.BancoException;
import br.com.escritorio.advocacia.util.DataUtil;
import br.com.escritorio.advocacia.util.JPAUtil;

public class GenericDAO {
	
	public static final String ORDER_BY_ASC = "ASC";
	public static final String ORDER_BY_DESC = "DESC";

	private EntityManager entityManager = JPAUtil.getEntityManager();
	
	/**
	 * Metodo para inserir uma lista de entidades no banco sem efetuar o commit
	 * @param entidade Entidades para serem inseridas
	 * @throws BancoException 
	 */
	@SuppressWarnings("unchecked")
	public List<Entidade> inserirSemCommit(List<?> entidades) throws BancoException {
		for (Object entidade : entidades) {
			inserirSemCommit((Entidade)entidade);
		}
		return (List<Entidade>) entidades;
	}
	
	/**
	 * Metodo para inserir uma entidade no banco sem efetuar o commit
	 * @param entidade Entidade para ser inserida
	 */
	public Entidade inserirSemCommit(Entidade entidade) throws BancoException{
		getEntityManager().persist(entidade);
		return entidade;
	}
	
	/**
	 * Metodo para inserir uma lista de entidades no banco
	 * @param entidade Entidades para serem inseridas
	 */
	@SuppressWarnings("unchecked")
	public List<Entidade> inserir(List<?> entidades) throws BancoException{
		JPAUtil.beginTransaction();
		try {
			for (Object entidade : entidades) {
				getEntityManager().persist((Entidade)entidade);
			}
			JPAUtil.commitTransaction();
			return (List<Entidade>) entidades;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BancoException("Nao foi possivel inserir as entidades : "+entidades.get(0).getClass().getSimpleName(),e);
		}
	}
	
	/**
	 * Metodo para inserir uma entidade no banco
	 * @param entidade Entidade para ser inserida
	 */
	public Entidade inserir(Entidade entidade) throws BancoException{
		JPAUtil.beginTransaction();
		try {
			getEntityManager().persist(entidade);
			JPAUtil.commitTransaction();
			return entidade;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BancoException("Nao foi possivel inserir a entidade : "+entidade.getClass().getSimpleName(),e);
		}
	}
	
	/**
	 * Metodo para atualizar os dados de varias entidades sem efetuar o commit
	 * @param entidade Entidades para serem atualizadas
	 */
	public void atualizarSemCommit(List<?> entidades) {
		for (Object entidade : entidades) {
			atualizarSemCommit((Entidade)entidade);
		}
	}
	
	/**
	 * Metodo para atualizar os dados de uma entidade sem efetuar o commit
	 * @param entidade Entidade para ser atualizada
	 */
	public void atualizarSemCommit(Entidade entidade) {
		getEntityManager().merge(entidade);
	}
	
	/**
	 * Metodo para atualizar os dados de varias entidades
	 * @param entidade Entidades para serem atualizadas
	 */
	public void atualizar(List<?> entidades) throws BancoException{
		JPAUtil.beginTransaction();
		try {
			for (Object entidade : entidades) {
				getEntityManager().merge(entidade);
			}
			JPAUtil.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BancoException("Nao foi possivel atualizar as entidades : "+entidades.get(0).getClass().getSimpleName(),e);
		}
	}
	
	/**
	 * Metodo para atualizar os dados de uma entidade
	 * @param entidade Entidade para ser atualizada
	 */
	public void atualizar(Entidade entidade) throws BancoException{
		JPAUtil.beginTransaction();
		try {
			getEntityManager().merge(entidade);
			JPAUtil.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BancoException("Nao foi possivel atualizar a entidade : "+entidade.getClass().getSimpleName(),e);
		}
	}
	
	/**
	 * Metodo para remover uma entidade do banco sem efetuar commit
	 * @param entidade Entidade para ser removida
	 * @throws BancoException 
	 */
	public void removerSemCommit(Entidade entidade) throws BancoException {
		Entidade entidadeBanco = buscarPorID(entidade.getClass(), entidade.getId()); 
		getEntityManager().remove(entidadeBanco);
	}
	
	/**
	 * Metodo para remover varias entidades do banco sem efetuar commit
	 * @param entidade Entidades para serem removidas
	 */
	public void removerSemCommit(List<Entidade> entidades) throws BancoException{
		for (Entidade entidade : entidades) {
			removerSemCommit((Entidade)entidade);
		}
	}

	/**
	 * Metodo para remover uma entidade do banco
	 * @param entidade Entidade para ser removida
	 */
	public void remover(Entidade entidade) throws BancoException{
		JPAUtil.beginTransaction();
		try {
			Entidade entidadeBanco = buscarPorID(entidade.getClass(), entidade.getId()); 
			getEntityManager().remove(entidadeBanco);
			JPAUtil.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BancoException("Nao foi possivel remover a entidade : "+entidade.getClass().getSimpleName(),e);
		}
	}
	
	/**
	 * Metodo para remover varias entidades do banco
	 * @param entidade Entidades para serem removidas
	 */
	public void remover(List<Entidade> entidades) throws BancoException{
		JPAUtil.beginTransaction();
		try {
			for (Entidade entidade : entidades) {
				Entidade entidadeBanco = buscarPorID(entidade.getClass(), entidade.getId()); 
				getEntityManager().remove(entidadeBanco);
			}
			JPAUtil.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BancoException("Nao foi possivel remover as entidades : "+entidades.get(0).getClass().getSimpleName(),e);
		}
	}
	
	public void removerTodos(Class<?> entidade) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" DELETE FROM "+entidade.getClass());
		getEntityManager().createQuery(sql.toString());
	}
	
	/**
	 * Metodo para buscar uma entidade pelo id
	 * @param clazz Classe que sera pesquisada
	 * @param id id que sera pesquisado
	 * @return entidade que foi pesquisada
	 */
	public <E> E buscarPorID(Class<E> clazz, Object id) throws BancoException{
		E entidade = null;
		try {
			entidade = getEntityManager().find(clazz, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BancoException("Nao foi possivel buscar por id : "+clazz.getSimpleName()+"id pesquisa : "+id,e);
		} 
		return entidade;
	}

	/**
	 * Metodo para buscar todos os dados de uma entidade
	 * @param clazz Classe para ser pesquisada
	 * @return Lista contendo os dados recuperados
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> buscarTodos(Class<E> clazz) throws BancoException{
		List<E> entidades = null;
		try {
			entidades = getEntityManager().createQuery("from " + clazz.getName()).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BancoException("Nao foi possivel buscar todos: "+clazz.getSimpleName(),e);
		} 
		return entidades;
	}
	
	/**
	 * Metodo para buscar todos os dados de uma entidade
	 * @param clazz Classe para ser pesquisada
	 * @param coluna Campo para ser ordenada na pesquisa
	 * @return Lista contendo os dados recuperados
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> buscarTodosOrdenado(Class<E> clazz , String coluna , String ordenacao) throws BancoException{
		List<E> entidades = null;
		try {
			entidades = getEntityManager().createQuery("SELECT entidade FROM " + clazz.getName()+ " entidade ORDER BY entidade."+coluna+" "+ordenacao).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BancoException("Nao foi possivel buscar todos ordenado: "+clazz.getSimpleName(),e);
		} 
		return entidades;
	}
	
	/**
	 * Buscar todos os dados de acordo com a entidade passada
	 * @param entidade Entidade para ser pesquisada
	 * @return Consulta de acordo com a entidade passada
	 * 
	 * <p>
	 * Exemplo de uso.:
	 * <p>
	 * 		Suponhamos que voce tem um objeto Pessoa com os atributos nome e sexo
	 * 		Se voce passar o objeto Pessoa com o nome e o sexo preenchidos a query ira ficar 
	 * 		<p>
	 * 		SELECT entity FROM Pessoa entity WHERE 1=1 and entity.nome = {'pessoa.nome'} and entity.sexo = {'pessoa.sexo'}
	 * 		</p>
	 *		Agora se voce usar uma das anotacoes {@link SearchConfig} ou {@link SearchType} dentro da classe pessoa, o metodo ira buscar de acordo com o que voce especificou nas anotacoes
	 *		<p>
	 *		Exemplo, no atributo nome voce colocou @LIKE_END, entao na execucao do metodo, a query ira ficar
	 *		<p>
	 *		SELECT entity FROM Pessoa entity WHERE 1=1 and entity.nome like "{'pessoa.nome'}%" and entity.sexo = {'pessoa.sexo'}
	 *		</p>
	 *		E assim sucessivamente
	 *		</p>
	 * </p>
	 * </p>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> buscarPorFiltro(E entidade) throws BancoException{
		List<E> entidades = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("from ").append(entidade.getClass().getName()).append(" entity where 1=1");
		List<Field> fields = getClassFields(entidade.getClass());
		for (Field field : fields) {
			String fieldName = getFieldMappedName(field);
			Object methodReturn = getGetterMethodReturn(entidade, field);
			if (methodReturn != null) {

				if (methodReturn instanceof Entidade) {
					createSubEntitiesClauses("entity."+fieldName, sb, methodReturn, field, params);
				} else {
					sb.append(" and entity.");
					sb.append(fieldName);
					createWhereConditions(sb, params, field, methodReturn);
				}
			}
		}

		try {
			Query query = getEntityManager().createQuery(sb.toString());
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i + 1, params.get(i));
			}
			entidades = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BancoException("Nao foi possivel buscar por filtro: "+entidade.getClass().getSimpleName(),e);
		}
		
		return entidades;
	}

	/**
	 * Retorna os campos da classe, assim como os da classe pai.
	 */
	@SuppressWarnings("rawtypes")
	private List<Field> getClassFields(Class clazz) {
		List<Field> fields = new ArrayList<Field>();
		if (clazz.getSuperclass() != Object.class) {
			fields.addAll(getClassFields(clazz.getSuperclass()));
		}
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		return fields;
	}

	/**
	 * Cria as clausulas para sub entidades.
	 * Por exemplo: "usuario.perfil.id"
	 */
	private void createSubEntitiesClauses(String prefix, StringBuilder sb, Object methodReturn, Field field, List<Object> params) {
		List<Field> subfields = getClassFields(methodReturn.getClass());
		for (Field subfield : subfields) {
			String subfieldName = getFieldMappedName(subfield);
			Object submethodReturn = getGetterMethodReturn(methodReturn, subfield);
			if (submethodReturn != null) {
				if (submethodReturn instanceof Entidade) {
					createSubEntitiesClauses(prefix + "." +subfieldName, sb, submethodReturn, subfield, params);
				} else {
					sb.append(" and ");
					sb.append(prefix);
					sb.append(".");
					sb.append(subfieldName);
					createWhereConditions(sb, params, subfield, submethodReturn);
				}
			}
		}
	}

	/**
	 * Retorna o valor do metodo getter para o determinado campo.
	 */
	private Object getGetterMethodReturn(Object entity, Field field) {
		String fieldName = field.getName();
		String getterName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
		try {
			Method getterMethod = entity.getClass().getMethod(getterName, new Class[0]);
			return getterMethod.invoke(entity, new Object[0]);
		} catch (Exception e) {
			return null;
		} 	
	}

	/**
	 * Cria a condicao da clausula where baseando no preenchimento da annotation {@link SearchConfig}.
	 */
	private void createWhereConditions(StringBuilder sb, List<Object> params, Field field, Object methodReturn) {
		SearchConfig annotation = field.getAnnotation(SearchConfig.class);
		if (annotation != null) {
			sb.append(annotation.type().value());
			sb.append("?");
			if (annotation.type() == SearchType.LIKE) {
				params.add("%" + methodReturn + "%");
			} else if(annotation.type() == SearchType.LIKE_END){
				params.add(methodReturn + "%");
			} else if(annotation.type() == SearchType.LIKE_BEGIN){
				params.add("%" + methodReturn);
			} else {
				params.add(methodReturn);
			}
		} else {
			sb.append(" = ?");
			params.add(methodReturn);
		}
	}
	
	/**
	 * Retorna o nome do campo ou o nome mapeado pela annotation {@link SearchConfig}.
	 */
	private String getFieldMappedName(Field field) {
		SearchConfig annotation = field.getAnnotation(SearchConfig.class);
		if (annotation != null && !annotation.mappedName().isEmpty()) {
			return annotation.mappedName();
		}
		return field.getName();
	}
	
	/**
	 * Metodo para converter uma data para a data correspondente em oracle
	 * @param date Data para ser formatada
	 * @return String contendo a data formatada
	 */
	public String dataParaOracle(Date date){
		return DataUtil.sdf_DDMMYYYY.format(date);
	}
	
	/**
	 * Metodo para criar qualquer entidade que tenha chave primaria composta 
	 * @param type Classe que tenha a chave composta mapeada
	 * @return String contendo a entidade.pk correspondente
	 */
	public String criarEntidadePK(Class<?> type) {
		String nomeFormatada = type.getSimpleName();
		Character primeiroCarac = Character.toLowerCase(nomeFormatada.charAt(0));
		nomeFormatada = nomeFormatada.substring(1 , nomeFormatada.length());
		return "entidade."+primeiroCarac + nomeFormatada+"Pk";
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
