package com.ssafy.api.service;

import com.ssafy.api.dto.SessionMessageReqDto;
import com.ssafy.api.dto.SessionReqDto;
import com.ssafy.common.exception.CustomException;
import com.ssafy.common.exception.ErrorCode;
import com.ssafy.db.entity.Session;
import com.ssafy.db.entity.SessionMessage;
import com.ssafy.db.entity.SessionType;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.SessionMessageRepository;
import com.ssafy.db.repository.SessionRepository;
import com.ssafy.db.repository.SessionTypeRepository;
import com.ssafy.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service("SessionService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SessionService {
    private final SessionRepository sessionRepository;
    private final SessionTypeRepository sessionTypeRepository;
    private final SessionMessageRepository sessionMessageRepository;
    private final UserRepository userRepository;
    /*
     * description: 세션 생성
     * return: 생성된 세션
     * */
    @Transactional
    public Session createSession(SessionReqDto reqDto) {
        // 유효한 사용자인지 검증
        User user = userRepository.findByUserId(reqDto.getUserId()).
                orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 유효한 세션 타입인지 검증
        SessionType sessionType = sessionTypeRepository.findSessionTypeBySessionTypeId(reqDto.getSessionTypeId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SESSION_TYPE));

        // Sesison 생성
        Session session = Session.builder()
                .user(user)
                .name(reqDto.getName())
                .sessionTypeId(reqDto.getSessionTypeId())
                .expireTime(reqDto.getAnniversary().plusDays(7)) // 기념일 7일뒤 세션 만료
                .anniversary(reqDto.getAnniversary())
                .build();
        log.info("세션 생성");
        return sessionRepository.save(session);
    }

    /*
     * description: 세션 타입 조회
     * return: 세션 타입 리스트 반환
     * */
    public List<SessionType> getSessionTypeList() {
        return sessionTypeRepository.findAll();
    }

    /*
     * description: sessionId로 Session 조회
     * return: Session
     * */

    public Session getSession(Long sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
    }

    /*
     * description : 세션 메세지 생성을 위한 메소드
     * @param : userId - 메세지를 보낸 유저의 id
     * @return : user가 작성한 세션 메세지 목록
     */
    public List<Session> getSessionByUserId(Long userId) {
        return sessionRepository.findAllByUserUserIdOrderByAnniversary(userId).orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
    }

    /*
    * description : 세션 메세지 삭제를 위한 메소드
    * @param : sessionId - 메세지를 보낸 세션의 id
    * return : 성공 여부
    * */
    @Transactional
    public boolean deleteSession(Long sessionId) {
        Session session = findSession(sessionId);
        try {
            sessionRepository.delete(session);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /*
     * description: sessionId에 해당하는 세션메세지 리스트 조회
     * return: 세션메세지 리스트 반환
     * */
    public List<SessionMessage> getSessionMessage(Long sessionId) {
        Session session = findSession(sessionId);
        List<SessionMessage> sessionMessageList = session.getSessionMessagesList();
        return sessionMessageList;
    }

    /*
     * description: sessionMessageReqDto 로부터 얻은 정보로 세션메세지 생성
     * return: 생성된 세션메세지 반환
     * */
    @Transactional
    public SessionMessage createSessionMessage(SessionMessageReqDto sessionMessageReqDto) {
        Session session = findSession(sessionMessageReqDto.getSessionId());
        SessionMessage sessionMessage = null;
        // img를 첨부하지 않은 경우
        if (sessionMessageReqDto.getImg() == null) {
            SessionMessageReqDto.builder()
                    .img("default")
                    .build();
        }
        // img가 gifticon이 아닌 경우 (판별은 gifticonService에서 구현)
        sessionMessage = SessionMessage.builder()
                .session(session)
                .field(sessionMessageReqDto.getField())
                .expireTime(session.getAnniversary().plusDays(7))
                .nickname(sessionMessageReqDto.getNickname())
                .build();
        // gifticon인 경우 gifticonService에서 처리 (기프티콘 저장)
        // gifticonService.saveGifticon(sessionMessageReqDto.getImg(),session.getUserId()); // 기프티콘 저장(미구현) -> 기프티콘정보, 선물할 유저 아이디

        // sessionMessage 저장
        sessionMessageRepository.save(sessionMessage);

        return sessionMessage;
    }
    /*
     * description: sessionMessageId에 해당하는 세션메세지 상세 조회
     * return: 조회된 세션메세지 반환
     * */
    public SessionMessage getSessionMessageById(Long sessionMessageId) {
        return sessionMessageRepository.findBySessionMessageId(sessionMessageId).orElseThrow(() -> new CustomException(ErrorCode.SESSION_MESSAGE_NOT_FOUND));
    }

    /*
     * description: sessionId에 해당하는 세션유효성 체크
     * return: 세션유효성 여부 반환
     * */
    public Session findSession(Long sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
    }
    /*
     * description: Time이 유효한지 확인 (createTime < expireTime)
     * return: 유효한지에 대한 boolean 반환
     * */
    public boolean isValidTime(LocalDateTime createTime, LocalDateTime expireTime) {
        if (createTime.isAfter(expireTime)) {
            return false;
        }
        return true;
    }

    /*
     * description: 만료일이 현재보다 오래됐는지 체크 (expireTime < now), 스케줄러에 적용해야함
     * return: 오래됐다면 삭제메서드 호출
     * */
    public void checkExpireTime(LocalDate expireTime) {
        List<Session> sessionList = sessionRepository.findAll();
        for (Session session : sessionList) {
            if (session.getExpireTime().isBefore(LocalDate.now())) {
                deleteSession(session.getSessionId());
            }
        }
    }

}