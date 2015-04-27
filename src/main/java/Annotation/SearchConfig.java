package Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacao para pesquisas por filtro automatizando as consultas por sql.
 * 
 * Forma de usar:
 * @SearchConfig(type=SearchType.EQUALS) - ira buscar o registro igual -> tabela.registro = 'atributo';
 * @SearchConfig(type=SearchType.GREATER) - ira buscar todos os registro maiores -> tabela.registro > 'atributo'
 * @SearchConfig(type=SearchType.GREATER_EQUALS) - ira buscar todos os registro maiores e igual -> tabela.registro >= 'atributo'
 * @SearchConfig(type=SearchType.LESSER) - ira buscar todos os registros menores -> tabela.registro < 'atributo'
 * @SearchConfig(type=SearchType.LESSER_EQUALS) - ira buscar os registros menores ou iguais -> tabela.registro <= 'atributo'
 * @SearchConfig(type=SearchType.LIKE) - ira buscar todos os registros utilizando o like%%. -> tabela.registro like %'atributo'%
 * @SearchConfig(type=SearchType.LIKE_END) - ira buscar todos os registros utilizando o like%. -> tabela.registro like 'atributo'% 
 * @SearchConfig(type=SearchType.LIKE_BEGIN) - ira buscar todos os registros utilizando o like%. -> tabela.registro like %'atributo' 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SearchConfig {
	
	SearchType type() default SearchType.EQUALS;
	String mappedName() default "";

}
