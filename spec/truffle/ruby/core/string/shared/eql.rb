require File.expand_path('../../../../spec_helper', __FILE__)
require File.expand_path('../../fixtures/classes', __FILE__)

describe :string_eql_value, :shared => true do
  it "returns true if self <=> string returns 0" do
    'hello'.send(@method, 'hello').should be_true
  end

  it "returns false if self <=> string does not return 0" do
    "more".send(@method, "MORE").should be_false
    "less".send(@method, "greater").should be_false
  end

  it "ignores subclass differences" do
    a = "hello"
    b = StringSpecs::MyString.new("hello")

    a.send(@method, b).should be_true
    b.send(@method, a).should be_true
  end
end