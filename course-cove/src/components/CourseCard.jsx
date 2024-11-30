import React from 'react'; 
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card"; 
import { Badge } from "@/components/ui/badge"; 
import { Button } from "@/components/ui/button"; 
import { ExternalLink, Globe } from 'lucide-react';  

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
          <div className="flex flex-wrap gap-2 mb-4">
            {course.tags.map((tag, tagIndex) => (
              <Badge key={tagIndex} variant="outline">
                {tag}
              </Badge>
            ))}
          </div>
        </div>
        <Button
          variant="outline"
          className="w-full"
          onClick={(e) => {
            e.stopPropagation(); // Prevent card click when button is clicked
            window.open(course.url, '_blank');
          }}
        >
          <ExternalLink className="mr-2 h-4 w-4" />
          View Course
        </Button>
      </CardContent>
    </Card>
  ); 
};  

export default CourseCard;