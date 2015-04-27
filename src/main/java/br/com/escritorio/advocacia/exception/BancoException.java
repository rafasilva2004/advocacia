package br.com.escritorio.advocacia.exception;

public class BancoException extends CorporativoException{
	
	public BancoException(String descricaoErro) {
		super(descricaoErro);
	}
	
	public BancoException(String descricao , Exception exception) {
		super(descricao , exception);
	}

	private static final long serialVersionUID = 1L;

}
