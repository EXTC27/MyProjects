# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rails db:seed command (or created alongside the database with db:setup).
#
# Examples:
#
#   movies = Movie.create([{ name: 'Star Wars' }, { name: 'Lord of the Rings' }])
#   Character.create(name: 'Luke', movie: movies.first)


arr_subject = ["라면", "커피"]
arr_detail1 = ["일반라면", "짜장라면"]
arr_detail2 = ["캔커피", "믹스커피"]
arr_element1 = ["신라면", "진라면", "삼양라면", "안성탕면"]
arr_element2 = ["짜파게티", "진짜장", "짜왕"]
arr_element3 = ["레쓰비", "맥스웰하우스", "조지아"]
arr_element4 = ["맥심", "카누"]

arr_element1url = [
    "http://www.costco.co.kr/medias/sys_master/images/h64/h96/9867844452382.jpg",
    "http://jinramen.ottoginoodle.co.kr/views/asset/img/W/shot_project_1.png",
    "http://img.dongwonmall.com/dwmall/static_root/product_img/main/0006684/000668431_1_b.jpg",
    "http://kosinmall.com/web/product/big/201504/19376_shop1_496953.jpg"
    ]
arr_element2url = [
    "http://imgmaak.petaz.co.kr/maak-bucket/origin/20171107094838-gR7jZ.jpg",
    "https://www.skmart.co.uk/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/0/0/000_8814_1.jpg",
    "http://image.nongshim.com/non/pro/1492491267126.jpg"
    ]
arr_element3url = [
    "https://www.costco.co.kr/medias/sys_master/h67/h25/8810677174302.jpg",
    "http://img.danawa.com/prod_img/500000/474/037/img/4037474_1.jpg",
    "http://img.danawa.com/prod_img/500000/055/104/img/1104055_1.jpg"
    ]
arr_element4url = [
    "https://www.costco.co.kr/medias/sys_master/images/h2a/h73/9867794513950.jpg",
    "http://item.ssgcdn.com/16/42/65/item/0000008654216_i1_1200.jpg"
    ]


arr_subject.each do |i|
    s = Subject.new
    s.name = i
    s.save
end

arr_detail1.each do |i|
    s = Subject.find 1
    d = Detail.new
    d.subject = s
    d.name = i
    #d.remote_img_url = 'http://www.tycoonpost.com/news/photo/201602/1217_1257_3750.png'
    d.save
end

arr_detail2.each do |i|
    s = Subject.find 2
    d = Detail.new
    d.subject = s
    d.name = i
    d.save
end

arr_element1.each do |i|
    s = Subject.find 1
    d = Detail.find 1
    e = Element.new
    e.subject = s
    e.detail = d
    e.name = i
    e.save
end

arr_element2.each do |i|
    s = Subject.find 1
    d = Detail.find 2
    e = Element.new
    e.subject = s
    e.detail = d
    e.name = i
    e.save
end

arr_element3.each do |i|
    s = Subject.find 2
    d = Detail.find 3
    e = Element.new
    e.subject = s
    e.detail = d
    e.name = i
    e.save
end

arr_element4.each do |i|
    s = Subject.find 2
    d = Detail.find 4
    e = Element.new
    e.subject = s
    e.detail = d
    e.name = i
    e.save
end

####################### img삽입 ############################
count = 0
arr_element1url.each do |i|
    count = count + 1
    e = Element.find (count)
    e.remote_img_url = i
    e.save
end

arr_element2url.each do |i|
    count = count + 1
    e = Element.find (count)
    e.remote_img_url = i
    e.save
end

arr_element3url.each do |i|
    count = count + 1
    e = Element.find (count)
    e.remote_img_url = i
    e.save
end

arr_element4url.each do |i|
    count = count + 1
    e = Element.find (count)
    e.remote_img_url = i
    e.save
end


####################### info 삽입 ######################

e = Element.find 1
e.info = "브랜드 : 농심
  
신라면은 우리 입맛에 맞는 얼큰하고 매운맛 제품으로 농심의 정성과 앞선 기술 그리고 좋은 원료로 만들어 남녀노소 구분없이 많은 사랑을 받는 제품입니다. 

또한, 세계 여러나라에 수출하여 한국의 맛을 세계화 시키는 데 앞장서고 있습니다."

e.save