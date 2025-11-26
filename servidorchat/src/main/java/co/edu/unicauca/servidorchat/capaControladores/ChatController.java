package co.edu.unicauca.servidorchat.capaControladores;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import co.edu.unicauca.servidorchat.capaFachadaServices.DTO.MensajePublicoDTO;
import co.edu.unicauca.servidorchat.capaFachadaServices.DTO.MensajePrivadoDTO;

@Controller
public class ChatController {

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;
  
  // Mensajes grupales: el cliente envía a /apiChat/enviarGrupal y todos suscritos a /chatGrupal/sala reciben el mensaje
  @MessageMapping("/enviarMensajePublico")
  @SendTo("/chatGrupal/salaChatPublica")
  public MensajePublicoDTO enviarMensajeGrupal(MensajePublicoDTO mensaje) {
    mensaje.setContenido(mensaje.getNickname()+":"+mensaje.getContenido()); 
    mensaje.setFechaGeneracion(new Date()); 
    return mensaje; // reenviamos el mensaje a todos suscritos a /chatGrupal/sala
  }

  // Mensajes privados: cliente envía a /apiChat/enviarPrivado, backend envía a usuario específico con sendToUser()
  @MessageMapping("/enviarMensajePrivado")
  public void enviarMensajePrivado(MensajePrivadoDTO mensaje) {   
    String mensajeParaEnviar = mensaje.getNicknameOrigen() + ": " + mensaje.getContenido();
    mensaje.setContenido(mensajeParaEnviar); 
    simpMessagingTemplate.convertAndSend("/chatPrivado/" + mensaje.getNicknameDestino(), mensaje);

  }

 @MessageMapping("/enviarReaccion")  
  //@SendTo("/brokerDeReacciones/reaccionesPorCancion") // NO USAR
  public String processMessage(String message) {
      return message;
  }

   // ===================== PLAY =====================
  @MessageMapping("/iniciarReproduccion")
  @SendTo("/brokerDeReacciones/inician")
  public String iniciarReproduccion(String nickname) {
      return nickname;
  }

  // ===================== PAUSE =====================
  @MessageMapping("/detenerReproduccion")
  @SendTo("/brokerDeReacciones/detienen")
  public String detenerReproduccion(String nickname) {
      return nickname;
  }
}

