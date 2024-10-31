package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Member;
import com.example.demo.model.User;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/member-api")
public class MemberController {

	@Autowired
	private MemberRepository memberRepo;

	@Autowired
	private UserRepository userRepo;

	// ======= Get All Member =======
	@GetMapping("/list")
	public ResponseEntity<?> getListMember(@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "position", required = false) String position,
			@RequestParam(name = "createUser", required = false) String createUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Member> memberList = new ArrayList<Member>();
			if (name != null) {
				memberList = memberRepo.findByNameContaining(name);
			} else if (position != null) {
				memberList = memberRepo.findByPositionContaining(position);
			} else if (createUser != null) {
				memberList = memberRepo.findByCreateUserContaining(createUser);
			} else {
				memberList = memberRepo.findAll();
			}

			map.put("status", "Success");
			map.put("data", memberList);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception error) {
			map.put("status", "Failed");
			map.put("message", "Internal Error");
			map.put("messageError", error.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ======= Get Member by ID =======
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getMember(@PathVariable("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Optional<Member> memberData = memberRepo.findById(id);

			if (memberData.isPresent()) {
				map.put("status", "Success");
				map.put("data", memberData);
			} else {
				map.put("status", "Failed");
				map.put("data", memberData);
				map.put("message", "Member not exist");
			}
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception error) {
			map.put("status", "Failed");
			map.put("message", "Internal Error");
			map.put("messageError", error.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ======= Create / Register Member ========
	@PostMapping("/create")
	public ResponseEntity<?> createMember(@RequestParam("name") String name,
			@RequestParam("createUser") String createUser, @RequestParam("position") String position,
			@RequestParam(name="file", required=false) MultipartFile file, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(file);
		try {
			Member memberExist = memberRepo.findFirstByName(name);
			if (memberExist != null) {
				map.put("status", "Failed");
				map.put("message", "Name already exist");
			} else {
				User userExist = userRepo.findFirstByUsername(createUser);
				if (userExist != null) {
					map.put("status", "Success");
					map.put("message", "Member Created");
					Member dataNew = new Member();
					dataNew.setName(name);
					dataNew.setPosition(position);
					dataNew.setCreateUser(createUser);
					if (file != null) {
						dataNew.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
					}
					memberRepo.save(dataNew);
				} else {
					map.put("status", "Failed");
					map.put("message", "Please login user first");
				}
			}

			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			map.put("status", "Failed");
			map.put("message", "Internal Error");
			map.put("messageError", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ======= Edit Member ========
	@PostMapping("/edit/{id}")
	public ResponseEntity<?> editMember(@PathVariable("id") String id, @RequestBody Member memberData) {
		Map<String, Object> map = new HashMap<String, Object>();
		Optional<Member> data = memberRepo.findById(id);
		try {
			List<Member> memberExist = memberRepo.findByNameContaining(memberData.getName());
			if (memberExist.size() > 1) {
				map.put("status", "Failed");
				map.put("message", "Name already exist");
			} else {
				if (data.isPresent()) {
					Member dataEdit = data.get();
					dataEdit.setName(memberData.getName());
					dataEdit.setPosition(memberData.getPosition());
					map.put("status", "Success");
					map.put("message", "Data member edited");
					memberRepo.save(dataEdit);
				}
			}

			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			map.put("status", "Failed");
			map.put("message", "Internal Error");
			map.put("messageError", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ======= Delete Member ========
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Optional<Member> data = memberRepo.findById(id);
		try {
			if (data.isPresent()) {
				Member dataDelete = data.get();
				map.put("status", "Success");
				map.put("message", "Data member edited");
				memberRepo.delete(dataDelete);
			} else {
				map.put("status", "Failed");
				map.put("message", "Data member not exist");
			}
			return new ResponseEntity<>(map, HttpStatus.OK);

		} catch (Exception e) {
			map.put("status", "Failed");
			map.put("message", "Internal Error");
			map.put("messageError", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
