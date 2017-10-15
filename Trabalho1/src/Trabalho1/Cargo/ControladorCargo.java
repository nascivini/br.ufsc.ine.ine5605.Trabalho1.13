package Trabalho1.Cargo;

import Trabalho1.Principal.ControladorPrincipal;
import java.util.ArrayList;

/**
 *
 * @author Vinicius Nascimento
 * @author Marina Ribeiro Kodama
 * @author Marco Aurelio Geremias
 */
public class ControladorCargo {
    
    private final ControladorPrincipal controladorPrincipal;
    private final ArrayList<Cargo> cargos;
    private final TelaCargo telaCargo;
    

    public ControladorCargo(ControladorPrincipal controladorPrincipal) {
        this.cargos = new ArrayList<>();
        this.telaCargo = new TelaCargo(this);
        this.controladorPrincipal = controladorPrincipal;
    }
    
    public ArrayList<Cargo> getCargos() {
        return cargos;
    }

    public TelaCargo getTelaCargo() {
        return telaCargo;
    }
    
    public ControladorPrincipal getControladorPrincipal() {
        return this.controladorPrincipal;
    }
    /**
     * Recebe os dados do cargo a ser incluído via parâmetro, inclui o cargo na lista de cargos, e retorna o cargo incluído.
     * @param conteudo
     * @return Cargo
     */
    public Cargo incluirCargo(DadosCargo conteudo){
        if(this.cargos.isEmpty()){
            Cargo novo = new Cargo(conteudo);
            cargos.add(novo);
            return novo; 
        }
        
        else{
            for(Cargo cargoLista : this.cargos){
                if(!(cargoLista.getCodigo() == conteudo.codigo && cargoLista.getNome().equals(conteudo.nome))){}
                    Cargo novo = new Cargo(conteudo);
                    cargos.add(novo);
                    return novo;
                }   
        }
        return null;
    }
    
    /**
     * Exclui o cargo da lista de cargos com base no código informado via parâmetro.
     * @param codigo Codigo do cargo a ser excluído. 
     * @return True or false indicando se o cargo foi exclu[ido ou não.
     */
    public boolean excluirCargo(int codigo) {
        if (this.findCargoByCodigo(codigo) != null) {
            for (int i = 0; i < cargos.size(); i++) {
                if (cargos.get(i).getCodigo() == codigo) {
                    cargos.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Altera os dados de um cargo para os novos dados informados via parâmetro, com exceção de seu código.
     * O código de um cargo não pode ser alterado.
     * @param conteudo Conteúdo a ser alterado no cargo.
     * @return Cargo Retorna o cargo que sofreu alterações.
     */
    public Cargo alterarCargo(DadosCargo conteudo) {
        if (conteudo != null) {
            for (Cargo cargoLista : cargos) {
                if (cargoLista.getCodigo() == conteudo.codigo) {
                    cargoLista.setEhGerencial(conteudo.ehGerencial);
                    cargoLista.setPermiteAcesso(conteudo.permiteAcesso);
                    cargoLista.setNome(conteudo.nome);
                    //cargoLista.setHorario(conteudo.horarios);
                    
                    return cargoLista;
                } 
            }
        }
        return null;
    }
    
    /**
     * "Varre" a lista de cargos cadastrados, buscando por um cargo que contenha o código passado como parâmetro. Retorna um Cargo nulo, caso não o encontre, e o Cargo encontrado, caso o encontre.
     * @param codigo
     * @return Cargo
     */
    public Cargo findCargoByCodigo(int codigo){
        for(Cargo cargoAtual : cargos){
            if(cargoAtual.getCodigo() == codigo){
                return cargoAtual; 
            }
        }
        return null;
    }
    
    public Cargo findCargoByNome(String nome){
        for(Cargo cargoAtual : cargos){
            if(cargoAtual.getNome().equals(nome)){
                return cargoAtual;
            }
        }
        return null;
    }
    
    public void listarCargos(){
        for(Cargo cargoLista : cargos){
            System.out.println("Nome: " + cargoLista.getNome() + " | Código: " + cargoLista.getCodigo());
        }
    }

}
