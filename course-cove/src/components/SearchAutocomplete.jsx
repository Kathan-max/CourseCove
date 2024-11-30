import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { 
  Command, 
  CommandEmpty, 
  CommandGroup, 
  CommandInput, 
  CommandItem, 
  CommandList 
} from "@/components/ui/command";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { 
  Table, 
  TableBody, 
  TableCaption, 
  TableCell, 
  TableHead, 
  TableHeader, 
  TableRow 
} from "@/components/ui/table";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircle, Globe } from "lucide-react";

const CourseSearch = () => {
  const [search, setSearch] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [courseLevel, setCourseLevel] = useState('');
  const [tags, setTags] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [courses, setCourses] = useState([]);
  const [sortBy, setSortBy] = useState(null);
  const [searchWordCounts, setSearchWordCounts] = useState({});
  
  // New state for spell correction
  const [spellCorrectionSuggestion, setSpellCorrectionSuggestion] = useState(null);

  const handleSearch = async (value) => {
    setSearch(value);
    
    if (value.length > 1) {
      try {
        const suggestionsResponse = await axios.get(`http://localhost:8080/autocorrect?query=${value}`);
        setSuggestions(suggestionsResponse.data);
      } catch (error) {
        console.error('Error fetching suggestions:', error);
      }
    } else {
      setSuggestions([]);
    }
  };

  const performSort = async (sortOption) => {
    try {
      const response = await axios.get(`http://localhost:8080/sort?type=${sortOption}`);
      
      setCourses(response.data);
      setSortBy(sortOption);
    } catch (error) {
      console.log("Error Sorting: ", error);
    }
  };

  const fetchSearchWordCounts = async () => {
    try {
      const response = await axios.get('http://localhost:8080/search-word-counts');
      setSearchWordCounts(response.data);
    } catch (error) {
      console.log("Error fetching search word counts: ", error);
    }
  };

  const checkSpellCorrection = async () => {
    try {
      const response = await axios.get('http://localhost:8080/spell-check');
      // If a correction exists and is different from the current search
      if (response.data && response.data !== search) {
        setSpellCorrectionSuggestion(response.data);
      } else {
        setSpellCorrectionSuggestion(null);
      }
    } catch (error) {
      console.log("Error fetching spell correction:", error);
    }
  };

  const performSearch = async () => {
    try {
      const filterParams = {
        query: search,
        maxPrice: parseFloat(maxPrice) || null,
        courseLevel: courseLevel,
        tags: tags ? tags.split(',').map(tag => tag.trim()) : [],
      };
      
      const response = await axios.post('http://localhost:8080/search', filterParams);
      
      setCourses(response.data);
      setSortBy(null);
      
      // Fetch spell correction after search
      checkSpellCorrection();
      
      // Fetch updated search word counts after each search
      fetchSearchWordCounts();
    } catch (error) {
      console.log("Error Searching: ", error);
    }
  };

  // Fetch search word counts when component mounts
  useEffect(() => {
    fetchSearchWordCounts();
  }, []);

  // Course Card Component
  const CourseCard = ({ course }) => {   
    const handleCardClick = () => {
      window.open(course.url, '_blank');
    };

    return (     
      <Card 
        className="hover:shadow-lg transition duration-300 cursor-pointer"
        onClick={handleCardClick}
      >
        <CardHeader>
          <div className="flex justify-between items-center">
            <CardTitle>{course.title}</CardTitle>
            {course.platform && (
              <Badge variant="secondary" className="flex items-center gap-1">
                <Globe className="h-3 w-3" />
                {course.platform}
              </Badge>
            )}
          </div>
        </CardHeader>
        <CardContent>
          <p className="text-muted-foreground mb-4">{course.description}</p>
          
          <div className="grid grid-cols-2 gap-2 mb-4">
            <div>
              <p className="text-xs text-muted-foreground">Instructor</p>
              <p>{course.instructor}</p>
            </div>
            <div>
              <p className="text-xs text-muted-foreground">Level</p>
              <p>{course.level}</p>
            </div>
          </div>
          <div className="flex justify-between items-center">
            <div>
              <p className="text-xs text-muted-foreground">Price</p>
              <p className="text-green-600 font-bold">${course.price.toFixed(2)}</p>
            </div>
            <div>
              <p className="text-xs text-muted-foreground">Rating</p>
              <p className="text-yellow-600 font-bold">{course.rating.toFixed(1)}/5</p>
            </div>
          </div>
          <div className="mt-4">
            <p className="text-xs text-muted-foreground mb-2">Tags</p>
            <div className="flex flex-wrap gap-2">
              {course.tags.map((tag, tagIndex) => (
                <Badge key={tagIndex} variant="outline">
                  {tag}
                </Badge>
              ))}
            </div>
          </div>
        </CardContent>
      </Card>
    ); 
  };

  return (
    <div className="min-h-screen bg-white p-8">
      <div className="container mx-auto">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-800 mb-4">
            Course Search
          </h1>
          <p className="text-xl text-gray-600 max-w-2xl mx-auto">
            Find the perfect course for your learning journey. Filter, sort, and explore a wide range of educational opportunities.
          </p>
        </div>
        {/* Search and Filters Container */}
        <div className="mb-8 bg-gray-50 p-6 rounded-lg shadow-md">
          {/* Spell Correction Alert */}
          {spellCorrectionSuggestion && (
            <Alert variant="default" className="mb-4">
              <AlertCircle className="h-4 w-4" />
              <AlertTitle>Did you mean?</AlertTitle>
              <AlertDescription>
                Did you mean to search for{' '}
                <span 
                  className="font-bold text-blue-600 cursor-pointer hover:underline"
                  onClick={() => {
                    setSearch(spellCorrectionSuggestion);
                    setSpellCorrectionSuggestion(null);
                  }}
                >
                  {spellCorrectionSuggestion}
                </span>
                ?
              </AlertDescription>
            </Alert>
          )}

          <div className="flex space-x-4 mb-4">
            {/* Search Input with Autocomplete */}
            <Popover>
              <PopoverTrigger asChild>
                <Input
                  value={search}
                  onChange={(e) => handleSearch(e.target.value)}
                  placeholder="Search courses"
                  className="w-full"
                />
              </PopoverTrigger>
              <PopoverContent className="w-full p-0" align="start">
                <Command>
                  <CommandInput 
                    placeholder="Search suggestions..." 
                    value={search}
                    onValueChange={handleSearch}
                  />
                  <CommandList>
                    {suggestions.length > 0 ? (
                      <CommandGroup>
                        {suggestions.map((suggestion, index) => (
                          <CommandItem 
                            key={index} 
                            value={suggestion}
                            onSelect={() => {
                              setSearch(suggestion);
                              setSuggestions([]);
                            }}
                          >
                            {suggestion}
                          </CommandItem>
                        ))}
                      </CommandGroup>
                    ) : (
                      <CommandEmpty>No suggestions found.</CommandEmpty>
                    )}
                  </CommandList>
                </Command>
              </PopoverContent>
            </Popover>

            {/* Price Filter */}
            <Input 
              type="number" 
              value={maxPrice}
              onChange={(e) => setMaxPrice(e.target.value)}
              placeholder="Max Price"
              className="w-32"
            />
            
            {/* Course Level Filter */}
            <Select onValueChange={setCourseLevel} value={courseLevel}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder="Course Level" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="Beginner">Beginner</SelectItem>
                <SelectItem value="Intermediate">Intermediate</SelectItem>
                <SelectItem value="Advanced">Advanced</SelectItem>
              </SelectContent>
            </Select>
            
            {/* Tags Filter */}
            <Input 
              value={tags}
              onChange={(e) => setTags(e.target.value)}
              placeholder="Enter tags (comma-separated)"
              className="w-48"
            />
            
            <Button onClick={performSearch}>
              Search
            </Button>
          </div>

          {/* Sorting and Courses Grid Remain the Same */}
          <div className="flex space-x-4">
            <Button 
              variant={sortBy === 'price' ? 'default' : 'outline'}
              onClick={() => performSort('price')}
            >
              Sort by Price
            </Button>
            <Button 
              variant={sortBy === 'rating' ? 'default' : 'outline'}
              onClick={() => performSort('rating')}
            >
              Sort by Rating
            </Button>
          </div>
        </div>

        {/* Courses Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {courses.map((course, index) => (
            <CourseCard key={index} course={course} />
          ))}
        </div>
      </div>

      {/* Search Word Counts Table */}
      <div className="mt-8">
        <Card>
          <CardHeader>
            <CardTitle>Search Frequency</CardTitle>
          </CardHeader>
          <CardContent>
            <Table>
              <TableCaption>A list of recent search words and their frequencies</TableCaption>
              <TableHeader>
                <TableRow>
                  <TableHead>Search Word</TableHead>
                  <TableHead className="text-right">Count</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {Object.entries(searchWordCounts).map(([word, count]) => (
                  <TableRow key={word}>
                    <TableCell className="font-medium">{word}</TableCell>
                    <TableCell className="text-right">{count}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default CourseSearch;