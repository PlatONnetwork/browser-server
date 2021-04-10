package com.platon.browser.service;

import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dao.mapper.VoteMapper;
import com.platon.browser.request.proposal.VoteListRequest;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.proposal.VoteListResp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class VoteServiceTest {
	@Mock
	private VoteMapper voteMapper;

	@Spy
	private VoteService target;

	@Test
	public void queryByProposal() {
		ReflectionTestUtils.setField(target,"voteMapper",voteMapper);

		List<Vote> voteList = new ArrayList<>();
		Vote vote = new Vote();
		vote.setCreateTime(new Date());
		vote.setHash("0xsfsdf");
		vote.setNodeId("0xsfsfs");
		vote.setNodeName("test");
		vote.setOption(3);
		vote.setProposalHash("0xfsdf");
		vote.setTimestamp(new Date());
		vote.setUpdateTime(new Date());
		voteList.add(vote);
		when(voteMapper.selectByExample(any())).thenReturn(voteList);

		VoteListRequest request = new VoteListRequest();
		request.setOption("33");
		request.setProposalHash("0xb851e45c6894ecd2737aad0112001d64fd8e14877010767406665f5efe9f45e7");
		RespPage<VoteListResp> resp = target.queryByProposal(request);
		assertNotNull(resp);


	}

}
