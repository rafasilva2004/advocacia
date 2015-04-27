package br.com.escritorio.advocacia.exception;

public class CorporativoException extends Exception{
	
	private String descricaoErro;
	private Exception exception;

	private static final long serialVersionUID = 1L;
	
	public CorporativoException(String descricaoErro , Exception exception) {
		setDescricaoErro(descricaoErro);
		setException(exception);
	}
	
	public CorporativoException(String descricaoErro) {
		super(descricaoErro);
		setDescricaoErro(descricaoErro);
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}

	public String getDescricaoErro() {
		return descricaoErro;
	}

	public void setDescricaoErro(String descricaoErro) {
		this.descricaoErro = descricaoErro;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}
