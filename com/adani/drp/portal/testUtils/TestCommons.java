package com.adani.drp.portal.testUtils;

import com.adani.drp.portal.entities.attachments.MediaInfo;
import com.adani.drp.portal.entities.core.HOHInfo;
import com.adani.drp.portal.entities.core.MemberInfo;
import com.adani.drp.portal.entities.core.UnitInfo;
import com.adani.drp.portal.entities.core.structure.StructureInfo;
import com.adani.drp.portal.entities.history.UnitStatusHistory;
import com.adani.drp.portal.entities.history.UnitVisitHistory;
import com.adani.drp.portal.entities.survey.SurveyorName;
import com.adani.drp.portal.models.history.UnitStatusHistoryResponse;
import com.adani.drp.portal.models.history.UnitVisitHistoryResponse;
import com.adani.drp.portal.models.responses.attachments.CategoryList;
import com.adani.drp.portal.models.responses.attachments.UnitInfoAttachResponse;
import com.adani.drp.portal.models.responses.core.*;
import com.adani.drp.portal.models.responses.core.domainValues.DomainInfoResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TestCommons {

    private static ObjectMapper objectMapper;

    public static String readTestResourceFile(String path) throws IOException {
        try (InputStream inputStream = TestCommons.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream != null) {
                return new String(inputStream.readAllBytes());
            } else {
                throw new IOException("Resource not found: " + path);
            }
        }
    }

    public static List<HoHResponse> createSampleHoHResponseList(String path) {
        List<HoHResponse> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();

            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<HoHResponse>>() {
            });

            return customObjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return customObjectList;
        }
    }

    public static List<HOHInfo> createSampleHoHRepoList(List<HoHResponse> hoHResponse) {
        List<HOHInfo> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();

            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            customObjectList = objectMapper.convertValue(hoHResponse, new TypeReference<List<HOHInfo>>() {
            });

            return customObjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return customObjectList;
        }
    }

    public static List<MemberInfo> createSampleMembersRepoResponseList(List<MemberResponse> memberResponseList) {
        List<MemberInfo> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();

            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            customObjectList = objectMapper.convertValue(memberResponseList, new TypeReference<List<MemberInfo>>() {
            });

            return customObjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return customObjectList;
        }
    }

    public static List<MemberResponse> createSampleMembersResponseList(String path) {
        List<MemberResponse> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<MemberResponse>>() {
            });

            return customObjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return customObjectList;
        }
    }

    public static List<UnitResponse> createSampleGetUnitList(String path) {
        List<UnitResponse> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<UnitResponse>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static List<Map<String, Object>> createSampleGetUnitPagedResponse(String path) {
        List<Map<String, Object>> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<Map<String, Object>>>() {
            });

            log.info("customObjectList {}",customObjectList);
            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static UnitsPagedResponse createSampleUnitsResponseList(String pagePath, String listPath) {
        UnitsPagedResponse customPagedResponse = new UnitsPagedResponse();

        List<UnitListElement> unitList = new ArrayList<>();
        try {

            objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            String input = readTestResourceFile(listPath);

            unitList = objectMapper.readValue(input, new TypeReference<List<UnitListElement>>() {
            });

            Page<UnitListElement> pageMock = Mockito.mock(Page.class);

            Mockito.when(pageMock.getContent()).thenReturn(unitList);
            Mockito.when(pageMock.getNumber()).thenReturn(1);
//            Mockito.when(pageMock.getNumberOfElements()).thenReturn(unitList.size());
            Mockito.when(pageMock.getSize()).thenReturn(unitList.size());
            Mockito.when(pageMock.getTotalElements()).thenReturn((long) 209);
            Mockito.when(pageMock.getTotalPages()).thenReturn(15);
//            Mockito.when(pageMock.hasContent()).thenReturn(!unitList.isEmpty());
//            Mockito.when(pageMock.hasNext()).thenReturn(true);
            Mockito.when(pageMock.isFirst()).thenReturn(true);
            Mockito.when(pageMock.isLast()).thenReturn(false);

            customPagedResponse.setUnits(pageMock.getContent());
            customPagedResponse.setPageable(pageMock.getPageable());
            customPagedResponse.setLast(pageMock.isLast());
            customPagedResponse.setTotalElements(pageMock.getTotalElements());
            customPagedResponse.setTotalPages(pageMock.getTotalPages());
            customPagedResponse.setNumber(pageMock.getNumber());
            customPagedResponse.setSize(pageMock.getSize());
            customPagedResponse.setFirst(pageMock.isFirst());
            customPagedResponse.setEmpty(pageMock.isEmpty());

            return customPagedResponse;


        } catch (Exception e) {
            e.printStackTrace();
            return customPagedResponse;
        }
    }

    public static List<StructureInfoResponse> createGetUnitsWithCountByStructureGlobalIdResponse(String path) {
        List<StructureInfoResponse> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<StructureInfoResponse>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static List<StructureInfo> createGetUnitsWithCountByStructureGlobalIdRepoResponse(String path) {
        List<StructureInfo> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<StructureInfo>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static List<DomainInfoResponse> createGetDomainInfoResponse(String path) {
        List<DomainInfoResponse> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<DomainInfoResponse>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static List<UnitStatusHistoryResponse> createGetQCStatusByUnitGlobalIdResponse(String path) {
        List<UnitStatusHistoryResponse> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<UnitStatusHistoryResponse>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static List<UnitVisitHistoryResponse> createVisitHistoryByUnitGlobalIdResponse(String path) {
        List<UnitVisitHistoryResponse> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<UnitVisitHistoryResponse>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static List<UnitVisitHistoryResponse> createGetVisitHistoryByUnitGlobalIdResponse(String path) {
        List<UnitVisitHistoryResponse> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<UnitVisitHistoryResponse>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }


    public static List<UnitVisitHistory> createGetQCStatusByUnitGlobalIdRepoResponse(List<UnitVisitHistoryResponse> unitVisitHistoryResponse) {
        List<UnitVisitHistory> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();

            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            customObjectList = objectMapper.convertValue(unitVisitHistoryResponse, new TypeReference<List<UnitVisitHistory>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static List<UnitStatusHistory> createUnitQCHistoryRepoResponse(List<UnitStatusHistoryResponse> unitStatusHistoryResponsesResponse) {
        List<UnitStatusHistory> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();

            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            customObjectList = objectMapper.convertValue(unitStatusHistoryResponsesResponse, new TypeReference<List<UnitStatusHistory>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static List<UnitInfo> createGetQCStatusByUnitGlobalIdServiceResponse(String path) {
        List<UnitInfo> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<UnitInfo>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static List<String> createDropDownListResponse(String path) {
        List<String> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<String>>() {
            });

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
    }

    public static CategoryList sampleAttachmentList(String path) {
        CategoryList customObjectList = new CategoryList();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<CategoryList>() {
            });

            return customObjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return customObjectList;
        }
    }


    public static List<SurveyorName> sampleSurveyorList(String path) {
        List<SurveyorName> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<List<SurveyorName>>() {
            });

            return customObjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return customObjectList;
        }
    }

    public static List<String> sampleSurveyNames(String path) {
        List<String> userNamesList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            String input = readTestResourceFile(path);

            List<SurveyorName> surveyorList = objectMapper.readValue(input, new TypeReference<List<SurveyorName>>() {
            });

            for (SurveyorName surveyor : surveyorList) {
                userNamesList.add(surveyor.getUserName());
            }

            return userNamesList;
        } catch (Exception e) {
            e.printStackTrace();
            return userNamesList;
        }
    }

    public static CategoryList sampleHohAttachmentList(String path) {
        CategoryList customObjectList = new CategoryList();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<CategoryList>() {
            });

            return customObjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return customObjectList;
        }
    }

    public static CategoryList sampleMemberAttachmentList(String path) {
        CategoryList customObjectList = new CategoryList();
        try {
            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, new TypeReference<CategoryList>() {
            });

            return customObjectList;
        } catch (Exception e) {
            e.printStackTrace();
            return customObjectList;
        }
    }

    public static List<Object[]> convertJsonDomainInfoToList(String path) {
        List<Object[]> resultList = new ArrayList<>();
        try {

            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            List<Map<String, String>> jsonList = objectMapper.readValue(input, new TypeReference<List<Map<String, String>>>() {});

            for (Map<String, String> element : jsonList) {
                Object[] array = new Object[]{
                        element.get("domainType"),
                        element.get("domainName"),
                        element.get("description"),
                        element.get("code")
                };
                resultList.add(array);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static List<Object[]> convertJsonDomainInfoCode(String path) {
        List<Object[]> resultList = new ArrayList<>();
        try {

            objectMapper = new ObjectMapper();
            String input = readTestResourceFile(path);

            List<Map<String, String>> jsonList = objectMapper.readValue(input, new TypeReference<List<Map<String, String>>>() {});

            for (Map<String, String> element : jsonList) {
                Object[] array = new Object[]{
                        element.get("domainType"),
                        element.get("domainName"),
                        element.get("description"),
                        element.get("code")
                };
                resultList.add(array);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

	public static List<MediaInfo> createGetUnitsInfoAttachResponse(String path) {
        List<MediaInfo> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();

            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input,
                    new TypeReference<List<MediaInfo>>() {});

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
	}

//	public static List<UnitMediaAttach> ConvertUnitsInfoAttach(String path) {
//        List<UnitMediaAttach> customObjectList = new ArrayList<>();
//        try {
//            objectMapper = new ObjectMapper();
//            String input = readTestResourceFile(path);
//            customObjectList = objectMapper.readValue(input, new TypeReference<List<UnitMediaAttach>>() {
//            });
//
//            return customObjectList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return customObjectList;
//        }
//    }
	
	public static <T> List<T> genericToReadJsonFile(String path,Class<T> genericObject) {
		List<T> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();

            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input, objectMapper.getTypeFactory().constructCollectionType(List.class, genericObject));

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
	}

	public static List<UnitInfoAttachResponse> sampleunitInfoAttachResponse(String path) {
		List<UnitInfoAttachResponse> customObjectList = new ArrayList<>();
        try {
            objectMapper = new ObjectMapper();

            String input = readTestResourceFile(path);

            customObjectList = objectMapper.readValue(input,
                    new TypeReference<List<UnitInfoAttachResponse>>() {});

            return customObjectList;


        } catch (Exception e) {
            e.printStackTrace();

            return customObjectList;
        }
	}

    public static List<Object[]> convertJsonDocumentCount(String path) {

            List<Object[]> resultList = new ArrayList<>();
            try {

                objectMapper = new ObjectMapper();
                String input = readTestResourceFile(path);

                List<Map<String, String>> jsonList = objectMapper.readValue(input, new TypeReference<List<Map<String, String>>>() {});

                for (Map<String, String> element : jsonList) {
                    Object[] array = new Object[]{
                            element.get("documentCategory"),
                            element.get("documentCount")
                    };
                    resultList.add(array);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultList;
        }

}