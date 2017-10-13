package Trabalho1.Acesso;

import Trabalho1.Principal.ControladorPrincipal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Vinicius Cerqueira Nascimento
 * @author Marina Ribeiro Kodama
 * @author Marco Aurelio Geremias
 */
public class ControladorAcesso {

    private ArrayList<Acesso> acessos;
    private ControladorPrincipal controladorPrincipal;
    private TelaAcesso telaAcesso;

    /**
     * Recebe o controlador Principal como parametro para possibilitar a
     * comunicacao e cria um objeto da Classe ControladorAcesso
     *
     * @param controladorPrincipal
     * @return Retorna um objeto da Classe ControladorAcesso
     */
    public ControladorAcesso(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
    }

    public ArrayList<Acesso> getAcessos() {
        return acessos;
    }

    public ControladorPrincipal getControladorPrincipal() {
        return controladorPrincipal;
    }

    public TelaAcesso getTelaAcesso() {
        return telaAcesso;
    }

    /**
     * Recebe uma matricula e lista os acessos negados da mesma
     *
     * @param matricula
     *
     */
    public void findAcessosNegadosByMatricula(int matricula) {
        //ArrayList<Acesso> acessosNegadosMat = new ArrayList<Acesso>();
        SimpleDateFormat formatarHora = new SimpleDateFormat("HH:mm");
        for (Acesso acesso : acessos) {
            if (acesso.getMotivo() != MotivoAcesso.OK && acesso.getMatricula() == matricula) {
                //acessosNegadosMat.add(acesso);
                System.out.println("Matricula: " + acesso.getMatricula() + " | Horario: " + acesso.getHorario().setTime(formatarHora.parse(formatarHora.format(acesso.getHorario().getTime()))) + " | Motivo: " + acesso.getMotivo().getDescricao());
            }
        }
        //return acessosNegadosMat;
    }

    /**
     * Recebe um motivo e lista os acessos negados do mesmo
     *
     * @param motivo
     *
     */
    public void findAcessosNegadosByMotivo(MotivoAcesso motivo) {
        //ArrayList<Acesso> acessosNegadosMot = new ArrayList<Acesso>();
        SimpleDateFormat formatarHora = new SimpleDateFormat("HH:mm");
        for (Acesso acesso : acessos) {
            if (acesso.getMotivo() != MotivoAcesso.OK && acesso.getMotivo() == motivo) {
                //acessosNegadosMot.add(acesso);
                System.out.println("Matricula: " + acesso.getMatricula() + " | Horario: " + acesso.getHorario().setTime(formatarHora.parse(formatarHora.format(acesso.getHorario().getTime()))) + " | Motivo: " + acesso.getMotivo().getDescricao());
            }
        }
        //return acessosNegadosMot;
    }

    /**
     * Lista todos os acessos negados
     *
     *
     */
    public void findAcessosNegados() throws ParseException {
        //ArrayList<Acesso> acessosNegados = new ArrayList<Acesso>();
        SimpleDateFormat formatarHora = new SimpleDateFormat("HH:mm");
        for (Acesso acesso : acessos) {
            if (acesso.getMotivo() != MotivoAcesso.OK) {
                //acessosNegados.add(acesso);
                System.out.println("Matricula: " + acesso.getMatricula() + " | Horario: " + acesso.getHorario().setTime(formatarHora.parse(formatarHora.format(acesso.getHorario().getTime()))) + " | Motivo: " + acesso.getMotivo().getDescricao());
            }
        }
        //return acessosNegados;
    }

    /**
     * Recebe uma matricula e realiza a verificacao se a mesma esta apta a
     * acessar a porta
     *
     * @param matricula
     * @return Verdadeiro ou falso, dependendo se a matricula possui ou nao
     * acesso a porta
     */
    public Acesso verificaAcesso(int matricula) {
        Calendar dataAgora = Calendar.getInstance();
        if (this.controladorPrincipal.getControladorFuncionario().validaMatricula(matricula)) { //validou a matricula, logo possui um funcionario com essa matricula
            if (this.controladorPrincipal.getControladorFuncionario().findFuncionarioByMatricula(matricula).getCargo().isEhGerencial()) {
                Acesso acesso = new Acesso(dataAgora, matricula, MotivoAcesso.OK);
                this.acessos.add(acesso);
                return acesso; //cargo gerencial possui acesso em qualquer hora
            } else if (!this.controladorPrincipal.getControladorFuncionario().findFuncionarioByMatricula(matricula).getCargo().isPermiteAcesso()) {
                Acesso acesso = new Acesso(dataAgora, matricula, MotivoAcesso.PERMISSAO);
                this.acessos.add(acesso);
                return acesso; //nao possui permissao em qualquer horario
            } else if (this.controladorPrincipal.getControladorFuncionario().findFuncionarioByMatricula(matricula).getCargo().isPermiteAcesso()) {
                ArrayList<Calendar> listaHorariosCargo = this.controladorPrincipal.getControladorFuncionario().findFuncionarioByMatricula(matricula).getCargo().getHorarios();

                //SimpleDateFormat formatarHora = new SimpleDateFormat("HH:mm");
                //dataAgora.setTime(formatarHora.parse(formatarHora.format(dataAgora.getTime())));

                for (int i = 0; i < listaHorariosCargo.size(); i = i + 2) {
                    Calendar horaEntrada = listaHorariosCargo.get(i);
                    Calendar horaSaida = listaHorariosCargo.get(i + 1);

                    if (horaEntrada.getTime().before(horaSaida.getTime()) && horaEntrada.getTime().before(dataAgora.getTime()) && horaSaida.getTime().after(dataAgora.getTime())) {
                        Acesso acesso = new Acesso(dataAgora, matricula, MotivoAcesso.OK);
                        this.acessos.add(acesso);
                        return acesso;
                    } else if (horaEntrada.getTime().after(horaSaida.getTime())) {
                        if (horaSaida.HOUR_OF_DAY == dataAgora.HOUR_OF_DAY && horaSaida.MINUTE >= dataAgora.MINUTE) {
                            Acesso acesso = new Acesso(dataAgora, matricula, MotivoAcesso.OK);
                            this.acessos.add(acesso);
                            return acesso; //acesso horario especial, hora atual = hora saida, verificar minutos
                        } else if (horaEntrada.HOUR_OF_DAY > dataAgora.HOUR_OF_DAY && horaSaida.HOUR_OF_DAY > dataAgora.HOUR_OF_DAY) {
                            Acesso acesso = new Acesso(dataAgora, matricula, MotivoAcesso.OK);
                            this.acessos.add(acesso);
                            return acesso; //acesso horario especial, ex: 22h as 5h com acesso a 1h
                        } else if (horaEntrada.HOUR_OF_DAY < dataAgora.HOUR_OF_DAY && horaSaida.HOUR_OF_DAY < dataAgora.HOUR_OF_DAY) {
                            Acesso acesso = new Acesso(dataAgora, matricula, MotivoAcesso.OK);
                            this.acessos.add(acesso);
                            return acesso; //acesso horario especial, ex: 22h as 5h com acesso a 23h 
                        }
                    }
                }
                Acesso acesso = new Acesso(dataAgora, matricula, MotivoAcesso.ATRASADO);
                this.acessos.add(acesso);
                return acesso;
            }
        } else {
            Acesso acesso = new Acesso(dataAgora, matricula, MotivoAcesso.OUTRO);
            this.acessos.add(acesso);
            return acesso; //matricula nao encontrada, nao eh para acontecer nunca. 
        }
        return null; //nao eh para acontecer nunca.
    }

}
