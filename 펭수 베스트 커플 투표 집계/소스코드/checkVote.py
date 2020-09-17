def isValid(comment, candidatesDic):
    count = 0
    candi = ""
    for candidate in candidatesDic:
        for name in candidatesDic[candidate]:
            if comment.count(name):
                # print(candidate)
                candi = candidate
                count += 1
                break
    if count == 1:
        return candi  
    else:
        return "invalid"
