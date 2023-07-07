package com.training.library.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.MemberDto;
import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.helper.ExcelToDtoMapper;
import com.training.library.mapper.DtoToEntity;
import com.training.library.models.Member;
import com.training.library.models.Upload;
import com.training.library.repositories.MemberRepository;
import com.training.library.repositories.UploadRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	protected DtoToEntity dtoToEntityMapper;
	@Autowired
	private UploadRepository uploadRepository;
	public ResponseDto saveMembers(MultipartFile file, String username) throws CustomExceptionHandler, IOException, URISyntaxException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		ExcelToDtoMapper mapper = new ExcelToDtoMapper(file);
		List<MemberDto> membersDto = mapper.mapToList(MemberDto.class);
		List<Member> members = dtoToEntityMapper.memberDtoToMember(membersDto);

		Upload upload = new Upload();
		upload.setFileName(file.getOriginalFilename());
		upload.setUploadName(username);
		upload.setMembers(members);
		upload.setUploadDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		uploadRepository.save(upload);

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("Inserted rows: ", members.size());
		result.setResult(res);
		return result;
	}

	public Member findByMemberId(String email) {
		return memberRepository.findByEmail(email).get();
	}

	public Member getMember(Long memberId) {
		Optional<Member> member = memberRepository.findById(memberId);
		if(member.isPresent()) {
			return member.get();
		}
		return null;
	}

}
