require 'test_helper'

class RegistControllerTest < ActionDispatch::IntegrationTest
  test "should get loginpage" do
    get regist_loginpage_url
    assert_response :success
  end

end
